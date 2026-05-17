package com.example.vrtourism.entity;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import lombok.Data;
import java.time.LocalDateTime;
import java.util.List;

@Document(collection = "ai_itineraries")
@Data
public class AIItinerary {

    @Id
    private String id;

    private Long userId;

    private String destinationName;

    private String prompt;

    private String generatedContent; // The full AI response

    private LocalDateTime createdAt = LocalDateTime.now();
}
