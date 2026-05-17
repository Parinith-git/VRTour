package com.example.vrtourism.repository;

import com.example.vrtourism.entity.VREnvironment;
import org.springframework.data.mongodb.repository.MongoRepository;
import java.util.Optional;

public interface VREnvironmentRepository extends MongoRepository<VREnvironment, String> {
    Optional<VREnvironment> findByDestinationId(Long destinationId);
}
