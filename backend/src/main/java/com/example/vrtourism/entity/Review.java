package com.example.vrtourism.entity;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import lombok.Data;
import java.time.LocalDateTime;

@Document(collection = "reviews")
@Data
public class Review {

    @Id
    private String id;

    private Long userId;

    private Long destinationId;

    private Integer rating;

    private String comment;

    private LocalDateTime createdAt = LocalDateTime.now();
    
    // Add these for UI convenience
    private String userName;
}
