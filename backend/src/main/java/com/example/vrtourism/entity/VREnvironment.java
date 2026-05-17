package com.example.vrtourism.entity;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import lombok.Data;

@Document(collection = "vr_environments")
@Data
public class VREnvironment {

    @Id
    private String id;

    private Long destinationId;

    private String modelPath;
    
    private String texturePath;
    
    private String mapSize;
}
