package com.example.vrtourism.controller;

import com.example.vrtourism.entity.Destination;
import com.example.vrtourism.entity.TravelPackage;
import com.example.vrtourism.repository.DestinationRepository;
import com.example.vrtourism.repository.TravelPackageRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;

import static org.hamcrest.Matchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
class PackageControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private TravelPackageRepository packageRepository;

    @Autowired
    private DestinationRepository destinationRepository;

    private Destination testDest;

    @BeforeEach
    void setUp() {
        packageRepository.deleteAll();
        destinationRepository.deleteAll();

        testDest = new Destination();
        testDest.setDestinationName("Test Beach");
        testDest.setDescription("A lovely beach");
        testDest.setLocation("Maldives");
        testDest.setEnvironmentType("Beach");
        testDest = destinationRepository.save(testDest);
    }

    private TravelPackage createPackage(String name, BigDecimal price, String duration, Destination dest) {
        TravelPackage pkg = new TravelPackage();
        pkg.setPackageName(name);
        pkg.setPackageId(System.currentTimeMillis()); // Ensure unique packageId
        pkg.setPrice(price);
        pkg.setDuration(duration);
        pkg.setHotelDetails("5-Star Resort");
        pkg.setDestinationId(dest.getDestinationId());
        return packageRepository.save(pkg);
    }

    @Test
    void getAllPackages_shouldReturnAllPackages() throws Exception {
        createPackage("Luxury Escape", new BigDecimal("1200.00"), "5 Days", testDest);
        createPackage("Budget Trip", new BigDecimal("500.00"), "3 Days", testDest);

        mockMvc.perform(get("/api/packages"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)));
    }

    @Test
    void getPackagesByDestination_shouldFilterByDestination() throws Exception {
        createPackage("Beach Package", new BigDecimal("900.00"), "4 Days", testDest);

        // Create another destination with a package
        Destination otherDest = new Destination();
        otherDest.setDestinationName("Mountains");
        otherDest.setDescription("Snowy peaks");
        otherDest.setLocation("Alps");
        otherDest.setEnvironmentType("Snow");
        otherDest = destinationRepository.save(otherDest);
        createPackage("Ski Package", new BigDecimal("1500.00"), "7 Days", otherDest);

        mockMvc.perform(get("/api/packages/destination/" + testDest.getDestinationId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].packageName").value("Beach Package"));
    }

    @Test
    void getPackageById_shouldReturnPackage() throws Exception {
        TravelPackage pkg = createPackage("Special Deal", new BigDecimal("750.00"), "3 Days", testDest);

        mockMvc.perform(get("/api/packages/" + pkg.getPackageId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.packageName").value("Special Deal"))
                .andExpect(jsonPath("$.duration").value("3 Days"));
    }

    @Test
    void getPackageById_shouldReturn404ForNonExistent() throws Exception {
        mockMvc.perform(get("/api/packages/99999"))
                .andExpect(status().isNotFound());
    }
}
