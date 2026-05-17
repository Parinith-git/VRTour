package com.example.vrtourism.controller;

import com.example.vrtourism.entity.Booking;
import com.example.vrtourism.entity.TravelPackage;
import com.example.vrtourism.entity.User;
import com.example.vrtourism.repository.BookingRepository;
import com.example.vrtourism.repository.TravelPackageRepository;
import com.example.vrtourism.repository.UserRepository;
import com.example.vrtourism.service.TokenService;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/bookings")
@RequiredArgsConstructor
public class BookingController {

    private final BookingRepository bookingRepository;
    private final UserRepository userRepository;
    private final TravelPackageRepository packageRepository;
    private final TokenService tokenService;

    @PostMapping
    public ResponseEntity<?> createBooking(@RequestHeader("Authorization") String tokenHeader, @RequestBody BookingRequest request) {
        String token = tokenHeader.replace("Bearer ", "");
        Long userId = tokenService.getUserIdFromToken(token);
        
        if (userId == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid token");
        }

        Optional<User> user = userRepository.findByUserId(userId);
        Optional<TravelPackage> travelPackage = packageRepository.findByPackageId(request.getPackageId());

        if (user.isEmpty() || travelPackage.isEmpty()) {
            return ResponseEntity.badRequest().body("Invalid user or package");
        }

        Booking booking = new Booking();
        booking.setUserId(user.get().getUserId());
        booking.setPackageId(travelPackage.get().getPackageId());
        booking.setPaymentStatus("CONFIRMED"); // Auto confirm for demo purposes
        
        bookingRepository.save(booking);

        return ResponseEntity.ok(booking);
    }

    @GetMapping("/history")
    public ResponseEntity<?> getBookingHistory(@RequestHeader("Authorization") String tokenHeader) {
        String token = tokenHeader.replace("Bearer ", "");
        Long userId = tokenService.getUserIdFromToken(token);
        
        if (userId == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid token");
        }

        List<Booking> bookings = bookingRepository.findByUserId(userId);
        return ResponseEntity.ok(bookings);
    }
}

@Data
class BookingRequest {
    private Long packageId;
}
