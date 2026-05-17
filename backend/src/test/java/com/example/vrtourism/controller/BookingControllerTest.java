package com.example.vrtourism.controller;

import com.example.vrtourism.entity.Destination;
import com.example.vrtourism.entity.TravelPackage;
import com.example.vrtourism.entity.User;
import com.example.vrtourism.repository.BookingRepository;
import com.example.vrtourism.repository.DestinationRepository;
import com.example.vrtourism.repository.TravelPackageRepository;
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

import java.math.BigDecimal;
import java.util.Map;

import static org.hamcrest.Matchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
class BookingControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private BookingRepository bookingRepository;

    @Autowired
    private TravelPackageRepository packageRepository;

    @Autowired
    private DestinationRepository destinationRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private TokenService tokenService;

    private User testUser;
    private TravelPackage testPackage;
    private String validToken;

    @BeforeEach
    void setUp() {
        bookingRepository.deleteAll();
        userRepository.deleteAll();
        packageRepository.deleteAll();
        destinationRepository.deleteAll();

        testUser = new User();
        testUser.setName("Booking User");
        testUser.setEmail("booker@example.com");
        testUser.setPassword(passwordEncoder.encode("pass"));
        testUser = userRepository.save(testUser);

        validToken = tokenService.generateToken(testUser.getUserId());

        Destination dest = new Destination();
        dest.setDestinationName("Test Dest");
        dest.setDescription("Test");
        dest.setLocation("Test");
        dest.setEnvironmentType("Test");
        dest = destinationRepository.save(dest);

        testPackage = new TravelPackage();
        testPackage.setPackageName("Test Package");
        testPackage.setPackageId(101L);
        testPackage.setPrice(new BigDecimal("999.99"));
        testPackage.setDuration("5 Days");
        testPackage.setHotelDetails("Nice Hotel");
        testPackage.setDestinationId(dest.getDestinationId());
        testPackage = packageRepository.save(testPackage);
    }

    @Test
    void createBooking_shouldCreateBookingWithValidToken() throws Exception {
        Map<String, Object> request = Map.of("packageId", testPackage.getPackageId());

        mockMvc.perform(post("/api/bookings")
                .header("Authorization", "Bearer " + validToken)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.paymentStatus").value("CONFIRMED"));
    }

    @Test
    void createBooking_shouldRejectInvalidToken() throws Exception {
        Map<String, Object> request = Map.of("packageId", testPackage.getPackageId());

        mockMvc.perform(post("/api/bookings")
                .header("Authorization", "Bearer invalid-token")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isUnauthorized());
    }

    @Test
    void createBooking_shouldRejectInvalidPackageId() throws Exception {
        Map<String, Object> request = Map.of("packageId", 99999);

        mockMvc.perform(post("/api/bookings")
                .header("Authorization", "Bearer " + validToken)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void getBookingHistory_shouldReturnUserBookings() throws Exception {
        // Create a booking first
        Map<String, Object> request = Map.of("packageId", testPackage.getPackageId());
        mockMvc.perform(post("/api/bookings")
                .header("Authorization", "Bearer " + validToken)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)));

        mockMvc.perform(get("/api/bookings/history")
                .header("Authorization", "Bearer " + validToken))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].packageId").value(testPackage.getPackageId()));
    }

    @Test
    void getBookingHistory_shouldReturnEmptyForNewUser() throws Exception {
        mockMvc.perform(get("/api/bookings/history")
                .header("Authorization", "Bearer " + validToken))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(0)));
    }

    @Test
    void getBookingHistory_shouldRejectInvalidToken() throws Exception {
        mockMvc.perform(get("/api/bookings/history")
                .header("Authorization", "Bearer bad-token"))
                .andExpect(status().isUnauthorized());
    }
}
