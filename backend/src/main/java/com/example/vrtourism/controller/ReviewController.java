package com.example.vrtourism.controller;

import com.example.vrtourism.dto.ReviewRequest;
import com.example.vrtourism.entity.Destination;
import com.example.vrtourism.entity.Review;
import com.example.vrtourism.entity.User;
import com.example.vrtourism.repository.DestinationRepository;
import com.example.vrtourism.repository.ReviewRepository;
import com.example.vrtourism.repository.UserRepository;
import com.example.vrtourism.service.TokenService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api/reviews")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class ReviewController {

    private final ReviewRepository reviewRepository;
    private final UserRepository userRepository;
    private final DestinationRepository destinationRepository;
    private final TokenService tokenService;

    private User getAuthenticatedUser(String authHeader) {
        if (authHeader == null || !authHeader.startsWith("Bearer ")) return null;
        String token = authHeader.substring(7);
        Long userId = tokenService.getUserIdFromToken(token);
        if (userId == null) return null;
        // Use the numeric userId from MongoDB
        return userRepository.findByUserId(userId).orElse(null);
    }

    @GetMapping("/destination/{destId}")
    public ResponseEntity<List<Review>> getReviewsByDestination(@PathVariable Long destId) {
        // Use the flat destinationId finder
        return ResponseEntity.ok(reviewRepository.findByDestinationIdOrderByCreatedAtDesc(destId));
    }

    @GetMapping("/stats/{destId}")
    public ResponseEntity<?> getReviewStats(@PathVariable Long destId) {
        List<Review> reviews = reviewRepository.findByDestinationId(destId);
        
        int count = reviews.size();
        double avg = 0;
        int[] distribution = new int[6]; // 1-5 stars

        if (count > 0) {
            int sum = 0;
            for (Review r : reviews) {
                sum += r.getRating();
                if (r.getRating() >= 1 && r.getRating() <= 5) {
                    distribution[r.getRating()]++;
                }
            }
            avg = (double) sum / count;
        }

        Map<String, Object> stats = new HashMap<>();
        stats.put("averageRating", Math.round(avg * 10.0) / 10.0);
        stats.put("totalReviews", count);
        stats.put("distribution", distribution);
        
        return ResponseEntity.ok(stats);
    }

    @PostMapping
    public ResponseEntity<?> submitReview(@RequestHeader("Authorization") String authHeader, @RequestBody ReviewRequest request) {
        User user = getAuthenticatedUser(authHeader);
        if (user == null) return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Unauthorized");

        if (request.getRating() < 1 || request.getRating() > 5) {
            return ResponseEntity.badRequest().body("Rating must be between 1 and 5");
        }

        // Verify destination exists via numeric ID
        Optional<Destination> destOpt = destinationRepository.findByDestinationId(request.getDestinationId());
        if (destOpt.isEmpty()) return ResponseEntity.badRequest().body("Destination not found");

        Review review = new Review();
        review.setUserId(user.getUserId());
        review.setUserName(user.getName());
        review.setDestinationId(request.getDestinationId());
        review.setRating(request.getRating());
        review.setComment(request.getComment());
        
        reviewRepository.save(review);
        return ResponseEntity.ok("Review submitted successfully");
    }
}
