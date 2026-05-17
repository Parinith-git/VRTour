package com.example.vrtourism.controller;

import com.example.vrtourism.entity.Destination;
import com.example.vrtourism.entity.VREnvironment;
import com.example.vrtourism.repository.DestinationRepository;
import com.example.vrtourism.repository.VREnvironmentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/destinations")
@RequiredArgsConstructor
public class DestinationController {

    private final DestinationRepository destinationRepository;
    private final VREnvironmentRepository vrEnvironmentRepository;

    @GetMapping
    public ResponseEntity<List<Destination>> getAllDestinations() {
        return ResponseEntity.ok(destinationRepository.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Destination> getDestinationById(@PathVariable Long id) {
        // Use the numeric destinationId from the repo
        return destinationRepository.findByDestinationId(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/{id}/vr")
    public ResponseEntity<VREnvironment> getVREnvironment(@PathVariable Long id) {
        // Use the flat destinationId field in MongoDB
        return vrEnvironmentRepository.findByDestinationId(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
}
