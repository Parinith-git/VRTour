package com.example.vrtourism.entity;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import lombok.Data;

@Document(collection = "destinations")
@Data
public class Destination {

    @Id
    private String id; // Use String for MongoDB IDs

    private Long destinationId; // Maintain numeric ID for frontend compatibility

    private String destinationName;

    private String description;

    private String imagePath;
    
    private String environmentType;
    
    private String location;
    public Destination() {
        if (this.destinationId == null) {
            this.destinationId = System.currentTimeMillis();
        }
    }
}
