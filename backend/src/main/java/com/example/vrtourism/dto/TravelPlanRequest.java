package com.example.vrtourism.dto;

import lombok.Data;

@Data
public class TravelPlanRequest {
    private String origin;
    private Long destId;
    private String destination;
    private String startDate;
    private String endDate;
    private String style; // budget, comfortable, luxury
}
