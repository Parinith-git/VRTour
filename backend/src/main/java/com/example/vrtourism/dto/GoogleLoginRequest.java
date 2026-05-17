package com.example.vrtourism.dto;

import lombok.Data;

@Data
public class GoogleLoginRequest {
    private String email;
    private String name;
    private String picture;
}
