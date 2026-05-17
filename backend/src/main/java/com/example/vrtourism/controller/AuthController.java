package com.example.vrtourism.controller;

import com.example.vrtourism.dto.AuthRequest;
import com.example.vrtourism.dto.AuthResponse;
import com.example.vrtourism.dto.GoogleLoginRequest;
import com.example.vrtourism.dto.SignupRequest;
import com.example.vrtourism.entity.User;
import com.example.vrtourism.repository.UserRepository;
import com.example.vrtourism.service.TokenService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;
import java.util.UUID;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final TokenService tokenService;

    @PostMapping("/signup")
    public ResponseEntity<?> signup(@RequestBody SignupRequest request) {
        if (userRepository.findByEmail(request.getEmail()).isPresent()) {
            return ResponseEntity.badRequest().body("Email already exists");
        }

        User user = new User();
        user.setName(request.getName());
        user.setEmail(request.getEmail());
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        
        userRepository.save(user);

        return ResponseEntity.ok("User registered successfully");
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody AuthRequest request) {
        User user = userRepository.findByEmail(request.getEmail()).orElse(null);
        
        if (user != null && passwordEncoder.matches(request.getPassword(), user.getPassword())) {
            String token = tokenService.generateToken(user.getUserId());
            return ResponseEntity.ok(new AuthResponse(token, user.getUserId(), user.getName(), user.getEmail(), user.getProfilePicture()));
        }
        
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid credentials");
    }

    @PostMapping("/google")
    public ResponseEntity<?> googleLogin(@RequestBody GoogleLoginRequest request) {
        User user = userRepository.findByEmail(request.getEmail()).orElse(null);
        if (user != null) {
            if (request.getPicture() != null && !request.getPicture().equals(user.getProfilePicture())) {
                user.setProfilePicture(request.getPicture());
                userRepository.save(user);
            }
        } else {
            user = new User();
            user.setEmail(request.getEmail());
            user.setName(request.getName());
            user.setPassword(passwordEncoder.encode(UUID.randomUUID().toString())); // Random password
            user.setProfilePicture(request.getPicture());
            userRepository.save(user);
        }
        String token = tokenService.generateToken(user.getUserId());
        return ResponseEntity.ok(new AuthResponse(token, user.getUserId(), user.getName(), user.getEmail(), user.getProfilePicture()));
    }
}
