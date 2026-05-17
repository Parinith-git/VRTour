package com.example.vrtourism.entity;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import lombok.Data;
import java.time.LocalDateTime;
import java.util.Map;

@Document(collection = "saved_trips")
@Data
public class SavedTrip {

    @Id
    private String id;

    private Long userId;

    private String origin;

    private String destinationName;

    private String travelStyle;

    private Integer days;

    private Double totalBudget;

    private Map<String, Double> costBreakdown;

    private LocalDateTime savedAt = LocalDateTime.now();
}
