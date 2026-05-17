package com.example.vrtourism.service;

import com.example.vrtourism.dto.TravelPlanRequest;
import com.example.vrtourism.entity.Destination;
import com.example.vrtourism.repository.DestinationRepository;
import tools.jackson.databind.JsonNode;
import tools.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.*;

@Slf4j
@Service
public class GroqAiService {

    @Value("${groq.api.key}")
    private String apiKey;

    @Value("${groq.api.model}")
    private String model;

    private final DestinationRepository destinationRepository;
    private final ObjectMapper objectMapper;
    private final HttpClient httpClient;

    public GroqAiService(DestinationRepository destinationRepository, ObjectMapper objectMapper) {
        this.destinationRepository = destinationRepository;
        this.objectMapper = objectMapper;
        this.httpClient = HttpClient.newBuilder()
                .connectTimeout(Duration.ofSeconds(15))
                .build();
    }

    public Map<String, Object> generateTravelPlan(TravelPlanRequest request) {
        // Look up destination name from DB
        String destName = request.getDestination();
        if (destName == null && request.getDestId() != null) {
            Optional<Destination> dest = destinationRepository.findByDestinationId(request.getDestId());
            if (dest.isPresent()) {
                destName = dest.get().getDestinationName() + ", " + dest.get().getLocation();
            }
        }

        if (destName == null) {
            destName = "Unknown Destination";
        }

        // Calculate nights
        long nights = 5;
        try {
            LocalDate start = LocalDate.parse(request.getStartDate());
            LocalDate end = LocalDate.parse(request.getEndDate());
            nights = ChronoUnit.DAYS.between(start, end);
            if (nights < 1) nights = 1;
        } catch (Exception e) {
            log.warn("Could not parse dates, defaulting to 5 nights");
        }

        // ── Special handling: Space City (destId=7) — fixed futuristic estimates ──
        if (request.getDestId() != null && request.getDestId() == 7L) {
            return buildSpaceCityEstimate(request.getOrigin(), nights, request.getStyle());
        }

        String prompt = buildPrompt(request.getOrigin(), destName, nights, request.getStyle());

        try {
            String aiResponse = callGroqApi(prompt);
            return parseAiResponse(aiResponse, destName, request.getOrigin(), nights, request.getStyle());
        } catch (Exception e) {
            log.error("Groq API call failed: {}", e.getMessage(), e);
            return Map.of("error", "AI service temporarily unavailable. Please try again.");
        }
    }

    private Map<String, Object> buildSpaceCityEstimate(String origin, long nights, String style) {
        // Base prices in INR (crores converted to raw INR)
        long rocketTravel, spaceAccommodation, foodLifeSupport, training, insurance, misc;

        switch (style != null ? style : "comfortable") {
            case "budget" -> {
                // Basic Suborbital: ₹2cr – ₹5cr range
                rocketTravel = 20_00_00_000L;       // ₹2 crore
                spaceAccommodation = 5_00_00_000L;   // ₹50 lakh
                foodLifeSupport = 3_00_00_000L;      // ₹30 lakh
                training = 8_00_00_000L;             // ₹80 lakh
                insurance = 5_00_00_000L;            // ₹50 lakh
                misc = 2_00_00_000L;                 // ₹20 lakh
            }
            case "luxury" -> {
                // Multi-Day Space City Vacation: ₹80cr – ₹250cr range
                long perNight = 20_00_00_000L;       // ₹20 crore per night
                rocketTravel = 80_00_00_000L;        // ₹80 crore
                spaceAccommodation = perNight * nights; // ₹20cr × nights
                foodLifeSupport = 5_00_00_000L * nights; // ₹5cr × nights
                training = 25_00_00_000L;            // ₹25 crore
                insurance = 15_00_00_000L;           // ₹15 crore
                misc = 10_00_00_000L;                // ₹10 crore
            }
            default -> {
                // Orbital Space Travel: ₹40cr – ₹100cr range
                long perNight = 8_00_00_000L;        // ₹8 crore per night
                rocketTravel = 40_00_00_000L;        // ₹40 crore
                spaceAccommodation = perNight * nights; // ₹8cr × nights
                foodLifeSupport = 2_00_00_000L * nights; // ₹2cr × nights
                training = 12_00_00_000L;            // ₹12 crore
                insurance = 8_00_00_000L;            // ₹8 crore
                misc = 5_00_00_000L;                 // ₹5 crore
            }
        }

        long total = rocketTravel + spaceAccommodation + foodLifeSupport + training + insurance + misc;

        Map<String, Object> result = new LinkedHashMap<>();
        result.put("destination", "Space Loop City, Low Earth Orbit");
        result.put("origin", origin);
        result.put("nights", nights);
        result.put("style", style);
        result.put("isSpaceDestination", true);
        result.put("flight", rocketTravel);
        result.put("hotel", spaceAccommodation);
        result.put("food", foodLifeSupport);
        result.put("transport", training);
        result.put("tickets", insurance);
        result.put("misc", misc);
        result.put("total", total);
        result.put("dailyAvg", nights > 0 ? total / nights : total);
        result.put("bestMonths", "Year-round (orbital windows every 2 weeks)");
        result.put("suggestedStay", "3–5 days for orbital, 7–14 days for deep space");
        result.put("nearby", List.of("Zero-G Observatory Deck", "Nebula Viewing Lounge", "Orbital Sunrise Point"));
        result.put("tips", List.of(
            "Complete 6 months of astronaut fitness training before departure",
            "Pack minimal — strict 10kg luggage limit per passenger",
            "Book the sunrise-side pod for the best Earth views"
        ));
        return result;
    }

    private String buildPrompt(String origin, String destination, long nights, String style) {
        return """
            You are an expert travel budget planner for Indian travelers. Estimate a REALISTIC travel budget in Indian Rupees (INR).

            TRIP DETAILS:
            - From: %s
            - To: %s
            - Duration: %d nights
            - Style: %s

            MANDATORY REFERENCE PRICES (use these as baselines — adjust for style):
            
            ROUND-TRIP FLIGHTS (Economy, per person):
            | Route                          | Price (INR)     |
            |-------------------------------|-----------------|
            | India domestic (e.g. BLR→DEL)  | ₹5,000–12,000   |
            | India → Southeast Asia         | ₹15,000–30,000  |
            | India → Middle East (Dubai)    | ₹18,000–35,000  |
            | India → Europe (Paris/Rome)    | ₹45,000–75,000  |
            | India → UK (London)            | ₹40,000–65,000  |
            | India → USA (New York)         | ₹55,000–90,000  |
            | India → Australia (Sydney)     | ₹50,000–85,000  |
            | India → Egypt (Cairo)          | ₹30,000–55,000  |
            | India → Japan                  | ₹35,000–60,000  |
            For Business class: multiply by 2.5–3x. For Luxury/First: multiply by 4–5x.

            HOTELS (per night):
            | Location          | Budget      | Comfortable    | Luxury         |
            |-------------------|-------------|----------------|----------------|
            | India             | ₹1,500–3,000| ₹4,000–8,000   | ₹12,000–30,000 |
            | Southeast Asia    | ₹2,000–4,000| ₹5,000–10,000  | ₹15,000–40,000 |
            | Europe            | ₹4,000–7,000| ₹8,000–18,000  | ₹25,000–60,000 |
            | USA               | ₹5,000–8,000| ₹10,000–20,000 | ₹30,000–70,000 |
            | Australia         | ₹4,500–7,500| ₹9,000–18,000  | ₹25,000–55,000 |
            | Egypt             | ₹2,000–4,000| ₹5,000–10,000  | ₹15,000–35,000 |

            FOOD (per day):
            | Location          | Budget       | Comfortable    | Luxury         |
            |-------------------|--------------|----------------|----------------|
            | India             | ₹500–1,000   | ₹1,500–3,000   | ₹4,000–8,000   |
            | Europe            | ₹2,000–3,500 | ₹4,000–7,000   | ₹8,000–15,000  |
            | USA               | ₹2,500–4,000 | ₹5,000–8,000   | ₹10,000–20,000 |
            | SE Asia/Egypt     | ₹800–1,500   | ₹2,000–4,000   | ₹5,000–10,000  |

            RULES:
            - Use the REFERENCE PRICES above as minimum baselines. DO NOT go below them.
            - Hotel = per-night rate × %d nights.
            - Food = per-day rate × %d days.
            - Transport: local daily transport (metro/taxi/uber).
            - Tickets: entry fees for major attractions at destination.
            - Misc: visa fees, travel insurance, SIM card, souvenirs.
            - All values MUST be in INR as integers. NO decimal points.

            Return ONLY this JSON (no markdown, no explanation, no code fences):
            {
              "flight": <integer>,
              "hotel": <integer>,
              "food": <integer>,
              "transport": <integer>,
              "tickets": <integer>,
              "misc": <integer>,
              "bestMonths": "<string>",
              "suggestedStay": "<string>",
              "nearby": ["<string>", "<string>", "<string>"],
              "tips": ["<string>", "<string>", "<string>"]
            }
            """.formatted(origin, destination, nights, style, nights, nights + 1);
    }

    private String callGroqApi(String prompt) throws Exception {
        // Build request body manually to avoid Jackson serialization issues
        String requestBody = """
            {
              "model": "%s",
              "messages": [{"role": "user", "content": %s}],
              "temperature": 0.7,
              "max_tokens": 800
            }
            """.formatted(model, objectMapper.writeValueAsString(prompt));

        HttpRequest httpRequest = HttpRequest.newBuilder()
                .uri(URI.create("https://api.groq.com/openai/v1/chat/completions"))
                .header("Content-Type", "application/json")
                .header("Authorization", "Bearer " + apiKey)
                .POST(HttpRequest.BodyPublishers.ofString(requestBody))
                .timeout(Duration.ofSeconds(30))
                .build();

        HttpResponse<String> response = httpClient.send(httpRequest, HttpResponse.BodyHandlers.ofString());

        if (response.statusCode() != 200) {
            log.error("Groq API error {}: {}", response.statusCode(), response.body());
            throw new RuntimeException("Groq API returned status " + response.statusCode());
        }

        // Extract the message content from Groq response
        JsonNode root = objectMapper.readTree(response.body());
        return root.at("/choices/0/message/content").asString();
    }

    private Map<String, Object> parseAiResponse(String aiText, String destination, String origin, long nights, String style) {
        try {
            // Clean up response — remove markdown code fences if present
            String cleaned = aiText.trim();
            if (cleaned.startsWith("```")) {
                cleaned = cleaned.replaceAll("```json\\s*", "").replaceAll("```\\s*", "");
            }

            JsonNode json = objectMapper.readTree(cleaned);

            int flight = json.get("flight").asInt();
            int hotel = json.get("hotel").asInt();
            int food = json.get("food").asInt();
            int transport = json.get("transport").asInt();
            int tickets = json.get("tickets").asInt();
            int misc = json.get("misc").asInt();
            int total = flight + hotel + food + transport + tickets + misc;

            Map<String, Object> result = new LinkedHashMap<>();
            result.put("destination", destination);
            result.put("origin", origin);
            result.put("nights", nights);
            result.put("style", style);
            result.put("flight", flight);
            result.put("hotel", hotel);
            result.put("food", food);
            result.put("transport", transport);
            result.put("tickets", tickets);
            result.put("misc", misc);
            result.put("total", total);
            result.put("dailyAvg", nights > 0 ? total / nights : total);
            result.put("bestMonths", json.has("bestMonths") ? json.get("bestMonths").asString() : "Year-round");
            result.put("suggestedStay", json.has("suggestedStay") ? json.get("suggestedStay").asString() : nights + " days");
            result.put("nearby", jsonArrayToList(json.get("nearby")));
            result.put("tips", jsonArrayToList(json.get("tips")));
            return result;
        } catch (Exception e) {
            log.error("Failed to parse AI response: {}", aiText, e);
            throw new RuntimeException("Could not parse AI response");
        }
    }

    private List<String> jsonArrayToList(JsonNode arrayNode) {
        List<String> list = new ArrayList<>();
        if (arrayNode != null && arrayNode.isArray()) {
            arrayNode.forEach(node -> list.add(node.asString()));
        }
        return list;
    }
}
