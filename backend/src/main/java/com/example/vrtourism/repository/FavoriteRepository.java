package com.example.vrtourism.repository;

import com.example.vrtourism.entity.Favorite;
import org.springframework.data.mongodb.repository.MongoRepository;
import java.util.List;
import java.util.Optional;

public interface FavoriteRepository extends MongoRepository<Favorite, String> {
    List<Favorite> findByUserId(Long userId);
    Optional<Favorite> findByUserIdAndDestinationId(Long userId, Long destinationId);
}
