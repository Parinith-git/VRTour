package com.example.vrtourism.dto;

import lombok.Data;

@Data
public class AuthResponse {
    private String token;
    private Long userId;
    private String name;
    private String email;
    private String profilePicture;

    public AuthResponse(String token, Long userId, String name, String email, String profilePicture) {
        this.token = token;
        this.userId = userId;
        this.name = name;
        this.email = email;
        this.profilePicture = profilePicture;
    }
}
