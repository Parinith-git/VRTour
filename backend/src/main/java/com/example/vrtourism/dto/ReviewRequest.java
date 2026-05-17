package com.example.vrtourism.dto;

import lombok.Data;

@Data
public class ReviewRequest {
    private Long destinationId;
    private Integer rating;
    private String comment;
}
