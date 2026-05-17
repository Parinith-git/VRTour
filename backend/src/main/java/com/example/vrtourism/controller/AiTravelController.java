package com.example.vrtourism.controller;

import com.example.vrtourism.dto.TravelPlanRequest;
import com.example.vrtourism.service.GroqAiService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/ai")
@RequiredArgsConstructor
public class AiTravelController {

    private final GroqAiService groqAiService;

    @PostMapping("/travel-plan")
    public ResponseEntity<Map<String, Object>> generateTravelPlan(@RequestBody TravelPlanRequest request) {
        Map<String, Object> result = groqAiService.generateTravelPlan(request);
        if (result.containsKey("error")) {
            return ResponseEntity.internalServerError().body(result);
        }
        return ResponseEntity.ok(result);
    }
}
