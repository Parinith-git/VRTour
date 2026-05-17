package com.example.vrtourism.repository;

import com.example.vrtourism.entity.TravelPackage;
import org.springframework.data.mongodb.repository.MongoRepository;
import java.util.List;
import java.util.Optional;

public interface TravelPackageRepository extends MongoRepository<TravelPackage, String> {
    List<TravelPackage> findByDestinationId(Long destinationId);
    Optional<TravelPackage> findByPackageId(Long packageId);
}
