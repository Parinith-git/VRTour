package com.example.vrtourism.entity;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import lombok.Data;
import java.time.LocalDateTime;

@Document(collection = "bookings")
@Data
public class Booking {

    @Id
    private String id;

    private Long userId;

    private Long packageId;

    private LocalDateTime bookingDate = LocalDateTime.now();

    private String paymentStatus = "PENDING";
}
