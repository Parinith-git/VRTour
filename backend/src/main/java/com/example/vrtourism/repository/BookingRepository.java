package com.example.vrtourism.repository;

import com.example.vrtourism.entity.Booking;
import org.springframework.data.mongodb.repository.MongoRepository;
import java.util.List;

public interface BookingRepository extends MongoRepository<Booking, String> {
    List<Booking> findByUserId(Long userId);
}
