package com.example.vrtourism.controller;

import com.example.vrtourism.entity.Destination;
import com.example.vrtourism.entity.VREnvironment;
import com.example.vrtourism.repository.DestinationRepository;
import com.example.vrtourism.repository.VREnvironmentRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import static org.hamcrest.Matchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
class DestinationControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private DestinationRepository destinationRepository;

    @Autowired
    private VREnvironmentRepository vrEnvironmentRepository;

    @BeforeEach
    void setUp() {
        destinationRepository.deleteAll();
        vrEnvironmentRepository.deleteAll();
    }

    private Destination createDestination(String name, String location, String envType) {
        Destination dest = new Destination();
        dest.setDestinationName(name);
        dest.setDescription("A beautiful " + name);
        dest.setImagePath("/assets/test.jpg");
        dest.setEnvironmentType(envType);
        dest.setLocation(location);
        return destinationRepository.save(dest);
    }

    @Test
    void getAllDestinations_shouldReturnEmptyListWhenNoData() throws Exception {
        mockMvc.perform(get("/api/destinations"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(0)));
    }

    @Test
    void getAllDestinations_shouldReturnAllDestinations() throws Exception {
        createDestination("Taj Mahal", "India", "Temple");
        createDestination("Eiffel Tower", "France", "City");

        mockMvc.perform(get("/api/destinations"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].destinationName", is("Taj Mahal")))
                .andExpect(jsonPath("$[1].destinationName", is("Eiffel Tower")));
    }

    @Test
    void getDestinationById_shouldReturnDestination() throws Exception {
        Destination dest = createDestination("Great Wall", "China", "Historical");

        mockMvc.perform(get("/api/destinations/" + dest.getDestinationId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.destinationName").value("Great Wall"))
                .andExpect(jsonPath("$.location").value("China"));
    }

    @Test
    void getDestinationById_shouldReturn404ForNonExistent() throws Exception {
        mockMvc.perform(get("/api/destinations/99999"))
                .andExpect(status().isNotFound());
    }

    @Test
    void getVREnvironment_shouldReturnVRData() throws Exception {
        Destination dest = createDestination("Colosseum", "Italy", "Historical");

        VREnvironment vr = new VREnvironment();
        vr.setDestinationId(dest.getDestinationId());
        vr.setModelPath("/models/colosseum.glb");
        vr.setTexturePath(null);
        vr.setMapSize("Medium");
        vrEnvironmentRepository.save(vr);

        mockMvc.perform(get("/api/destinations/" + dest.getDestinationId() + "/vr"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.modelPath").value("/models/colosseum.glb"))
                .andExpect(jsonPath("$.mapSize").value("Medium"));
    }

    @Test
    void getVREnvironment_shouldReturn404WhenNoVRExists() throws Exception {
        Destination dest = createDestination("No VR Place", "Nowhere", "None");

        mockMvc.perform(get("/api/destinations/" + dest.getDestinationId() + "/vr"))
                .andExpect(status().isNotFound());
    }
}
