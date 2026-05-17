package com.example.vrtourism.entity;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import lombok.Data;
import java.time.LocalDateTime;

@Document(collection = "favorites")
@Data
public class Favorite {

    @Id
    private String id;

    private Long userId;

    private Long destinationId;

    private LocalDateTime createdAt = LocalDateTime.now();
}
