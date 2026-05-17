package com.example.vrtourism.service;

import org.springframework.stereotype.Service;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class TokenService {
    // In-memory token store for simplicity in this academic project
    private final Map<String, Long> tokenStore = new ConcurrentHashMap<>();

    public String generateToken(Long userId) {
        String token = UUID.randomUUID().toString();
        tokenStore.put(token, userId);
        return token;
    }

    public Long getUserIdFromToken(String token) {
        return tokenStore.get(token);
    }
    
    public void invalidateToken(String token) {
        tokenStore.remove(token);
    }
}
