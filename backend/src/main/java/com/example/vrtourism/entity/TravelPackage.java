package com.example.vrtourism.entity;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import lombok.Data;
import java.math.BigDecimal;

@Document(collection = "packages")
@Data
public class TravelPackage {

    @Id
    private String id;

    private Long packageId;

    private Long destinationId;

    private String packageName;

    private BigDecimal price;

    private String duration;

    private String hotelDetails;
}
