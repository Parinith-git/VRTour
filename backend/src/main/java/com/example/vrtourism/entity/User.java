package com.example.vrtourism.entity;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import lombok.Data;
import java.time.LocalDateTime;

@Document(collection = "users")
@Data
public class User {

    @Id
    private String id;

    private Long userId;

    private String name;

    private String email;

    private String password;

    private String profilePicture;

    private LocalDateTime createdAt = LocalDateTime.now();
    public User() {
        if (this.userId == null) {
            this.userId = System.currentTimeMillis();
        }
    }
}
