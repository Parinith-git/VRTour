package com.example.vrtourism.repository;

import com.example.vrtourism.entity.Destination;
import org.springframework.data.mongodb.repository.MongoRepository;
import java.util.Optional;

public interface DestinationRepository extends MongoRepository<Destination, String> {
    Optional<Destination> findByDestinationId(Long destinationId);
}
