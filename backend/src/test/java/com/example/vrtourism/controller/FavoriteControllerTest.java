package com.example.vrtourism.controller;

import com.example.vrtourism.entity.Destination;
import com.example.vrtourism.entity.User;
import com.example.vrtourism.repository.DestinationRepository;
import com.example.vrtourism.repository.FavoriteRepository;
import com.example.vrtourism.repository.UserRepository;
import com.example.vrtourism.service.TokenService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import static org.hamcrest.Matchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
class FavoriteControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private FavoriteRepository favoriteRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private DestinationRepository destinationRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private TokenService tokenService;

    private User testUser;
    private Destination testDest;
    private String validToken;

    @BeforeEach
    void setUp() {
        favoriteRepository.deleteAll();
        userRepository.deleteAll();
        destinationRepository.deleteAll();

        testUser = new User();
        testUser.setName("Fav User");
        testUser.setEmail("fav@example.com");
        testUser.setPassword(passwordEncoder.encode("pass"));
        testUser = userRepository.save(testUser);

        validToken = tokenService.generateToken(testUser.getUserId());

        testDest = new Destination();
        testDest.setDestinationName("Fav Destination");
        testDest.setDescription("A lovely place");
        testDest.setLocation("Somewhere");
        testDest.setEnvironmentType("Beach");
        testDest = destinationRepository.save(testDest);
    }

    @Test
    void addFavorite_shouldAddDestinationToFavorites() throws Exception {
        mockMvc.perform(post("/api/favorites/" + testDest.getDestinationId())
                .header("Authorization", "Bearer " + validToken))
                .andExpect(status().isOk());
    }

    @Test
    void addFavorite_shouldRejectDuplicate() throws Exception {
        // Add once
        mockMvc.perform(post("/api/favorites/" + testDest.getDestinationId())
                .header("Authorization", "Bearer " + validToken));

        // Add again - should fail
        mockMvc.perform(post("/api/favorites/" + testDest.getDestinationId())
                .header("Authorization", "Bearer " + validToken))
                .andExpect(status().isBadRequest());
    }

    @Test
    void addFavorite_shouldRejectInvalidToken() throws Exception {
        mockMvc.perform(post("/api/favorites/" + testDest.getDestinationId())
                .header("Authorization", "Bearer invalid"))
                .andExpect(status().isUnauthorized());
    }

    @Test
    void addFavorite_shouldRejectNonExistentDestination() throws Exception {
        mockMvc.perform(post("/api/favorites/99999")
                .header("Authorization", "Bearer " + validToken))
                .andExpect(status().isBadRequest());
    }

    @Test
    void getUserFavorites_shouldReturnFavorites() throws Exception {
        // Add a favorite first
        mockMvc.perform(post("/api/favorites/" + testDest.getDestinationId())
                .header("Authorization", "Bearer " + validToken));

        mockMvc.perform(get("/api/favorites")
                .header("Authorization", "Bearer " + validToken))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].destinationName").value("Fav Destination"));
    }

    @Test
    void getUserFavorites_shouldReturnEmptyWhenNoFavorites() throws Exception {
        mockMvc.perform(get("/api/favorites")
                .header("Authorization", "Bearer " + validToken))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(0)));
    }

    @Test
    void removeFavorite_shouldRemoveFromFavorites() throws Exception {
        // Add first
        mockMvc.perform(post("/api/favorites/" + testDest.getDestinationId())
                .header("Authorization", "Bearer " + validToken));

        // Remove
        mockMvc.perform(delete("/api/favorites/" + testDest.getDestinationId())
                .header("Authorization", "Bearer " + validToken))
                .andExpect(status().isOk());

        // Verify empty
        mockMvc.perform(get("/api/favorites")
                .header("Authorization", "Bearer " + validToken))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(0)));
    }

    @Test
    void removeFavorite_shouldReturnBadRequestWhenNotFavorited() throws Exception {
        mockMvc.perform(delete("/api/favorites/" + testDest.getDestinationId())
                .header("Authorization", "Bearer " + validToken))
                .andExpect(status().isBadRequest());
    }
}
