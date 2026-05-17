package com.example.vrtourism.repository;

import com.example.vrtourism.entity.Review;
import org.springframework.data.mongodb.repository.MongoRepository;
import java.util.List;

public interface ReviewRepository extends MongoRepository<Review, String> {
    List<Review> findByDestinationIdOrderByCreatedAtDesc(Long destinationId);
    List<Review> findByDestinationId(Long destinationId);
}
