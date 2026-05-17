package com.example.vrtourism.controller;

import com.example.vrtourism.entity.Destination;
import com.example.vrtourism.entity.Favorite;
import com.example.vrtourism.entity.User;
import com.example.vrtourism.repository.DestinationRepository;
import com.example.vrtourism.repository.FavoriteRepository;
import com.example.vrtourism.repository.UserRepository;
import com.example.vrtourism.service.TokenService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/favorites")
@CrossOrigin(origins = "*")
public class FavoriteController {

    @Autowired
    private FavoriteRepository favoriteRepository;
    
    @Autowired
    private UserRepository userRepository;
    
    @Autowired
    private DestinationRepository destinationRepository;

    @Autowired
    private TokenService tokenService;

    private User getAuthenticatedUser(String authHeader) {
        if (authHeader == null || !authHeader.startsWith("Bearer ")) return null;
        String token = authHeader.substring(7);
        Long userId = tokenService.getUserIdFromToken(token);
        if (userId == null) return null;
        return userRepository.findByUserId(userId).orElse(null);
    }

    @GetMapping
    public ResponseEntity<?> getUserFavorites(@RequestHeader("Authorization") String authHeader) {
        User user = getAuthenticatedUser(authHeader);
        if (user == null) return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Unauthorized");

        List<Favorite> favorites = favoriteRepository.findByUserId(user.getUserId());
        List<Destination> favoriteDestinations = favorites.stream()
                .map(f -> destinationRepository.findByDestinationId(f.getDestinationId()).orElse(null))
                .filter(d -> d != null)
                .collect(Collectors.toList());
                
        return ResponseEntity.ok(favoriteDestinations);
    }

    @PostMapping("/{destId}")
    public ResponseEntity<?> addFavorite(@RequestHeader("Authorization") String authHeader, @PathVariable Long destId) {
        User user = getAuthenticatedUser(authHeader);
        if (user == null) return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Unauthorized");

        Optional<Destination> destOpt = destinationRepository.findByDestinationId(destId);
        if (destOpt.isEmpty()) return ResponseEntity.badRequest().body("Destination not found");

        Optional<Favorite> existingFav = favoriteRepository.findByUserIdAndDestinationId(user.getUserId(), destId);
        if (existingFav.isPresent()) return ResponseEntity.badRequest().body("Already in favorites");

        Favorite fav = new Favorite();
        fav.setUserId(user.getUserId());
        fav.setDestinationId(destId);
        favoriteRepository.save(fav);

        return ResponseEntity.ok("Added to favorites");
    }

    @DeleteMapping("/{destId}")
    public ResponseEntity<?> removeFavorite(@RequestHeader("Authorization") String authHeader, @PathVariable Long destId) {
        User user = getAuthenticatedUser(authHeader);
        if (user == null) return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Unauthorized");

        Optional<Favorite> existingFav = favoriteRepository.findByUserIdAndDestinationId(user.getUserId(), destId);
        if (existingFav.isPresent()) {
            favoriteRepository.delete(existingFav.get());
            return ResponseEntity.ok("Removed from favorites");
        }

        return ResponseEntity.badRequest().body("Favorite not found");
    }
}
