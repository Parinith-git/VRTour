package com.example.vrtourism.controller;

import com.example.vrtourism.entity.User;
import com.example.vrtourism.repository.UserRepository;
import com.example.vrtourism.service.TokenService;
import tools.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Map;

import static org.hamcrest.Matchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
class AuthControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @BeforeEach
    void setUp() {
        userRepository.deleteAll();
    }

    @Test
    void signup_shouldCreateNewUser() throws Exception {
        Map<String, String> request = Map.of(
            "name", "Test User",
            "email", "test@example.com",
            "password", "Password123!"
        );

        mockMvc.perform(post("/api/auth/signup")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk());

        // Verify user was saved
        assert userRepository.findByEmail("test@example.com").isPresent();
    }

    @Test
    void signup_shouldRejectDuplicateEmail() throws Exception {
        // Create a user first
        User existing = new User();
        existing.setName("Existing");
        existing.setEmail("duplicate@example.com");
        existing.setPassword(passwordEncoder.encode("pass"));
        userRepository.save(existing);

        Map<String, String> request = Map.of(
            "name", "Another User",
            "email", "duplicate@example.com",
            "password", "Password123!"
        );

        mockMvc.perform(post("/api/auth/signup")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void login_shouldReturnTokenForValidCredentials() throws Exception {
        // Create a user
        User user = new User();
        user.setName("Login User");
        user.setEmail("login@example.com");
        user.setPassword(passwordEncoder.encode("MySecret1!"));
        userRepository.save(user);

        Map<String, String> request = Map.of(
            "email", "login@example.com",
            "password", "MySecret1!"
        );

        mockMvc.perform(post("/api/auth/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.token").isNotEmpty())
                .andExpect(jsonPath("$.name").value("Login User"))
                .andExpect(jsonPath("$.email").value("login@example.com"));
    }

    @Test
    void login_shouldRejectInvalidPassword() throws Exception {
        User user = new User();
        user.setName("Test");
        user.setEmail("wrong@example.com");
        user.setPassword(passwordEncoder.encode("CorrectPass1!"));
        userRepository.save(user);

        Map<String, String> request = Map.of(
            "email", "wrong@example.com",
            "password", "WrongPassword!"
        );

        mockMvc.perform(post("/api/auth/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isUnauthorized());
    }

    @Test
    void login_shouldRejectNonExistentEmail() throws Exception {
        Map<String, String> request = Map.of(
            "email", "nobody@example.com",
            "password", "Whatever1!"
        );

        mockMvc.perform(post("/api/auth/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isUnauthorized());
    }

    @Test
    void googleLogin_shouldCreateNewUserAndReturnToken() throws Exception {
        Map<String, String> request = Map.of(
            "email", "google@gmail.com",
            "name", "Google User",
            "picture", "https://example.com/avatar.jpg"
        );

        mockMvc.perform(post("/api/auth/google")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.token").isNotEmpty())
                .andExpect(jsonPath("$.name").value("Google User"))
                .andExpect(jsonPath("$.email").value("google@gmail.com"))
                .andExpect(jsonPath("$.profilePicture").value("https://example.com/avatar.jpg"));
    }

    @Test
    void googleLogin_shouldReturnExistingUserToken() throws Exception {
        // Create user first
        User user = new User();
        user.setName("Existing Google");
        user.setEmail("existing@gmail.com");
        user.setPassword(passwordEncoder.encode("random"));
        userRepository.save(user);

        Map<String, String> request = Map.of(
            "email", "existing@gmail.com",
            "name", "Existing Google",
            "picture", "https://example.com/pic.jpg"
        );

        mockMvc.perform(post("/api/auth/google")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.token").isNotEmpty())
                .andExpect(jsonPath("$.email").value("existing@gmail.com"));
    }
}
