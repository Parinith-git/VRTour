package com.example.vrtourism.controller;

import com.example.vrtourism.entity.TravelPackage;
import com.example.vrtourism.repository.TravelPackageRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/packages")
@RequiredArgsConstructor
public class PackageController {

    private final TravelPackageRepository packageRepository;

    @GetMapping
    public ResponseEntity<List<TravelPackage>> getAllPackages() {
        return ResponseEntity.ok(packageRepository.findAll());
    }

    @GetMapping("/destination/{destId}")
    public ResponseEntity<List<TravelPackage>> getPackagesByDestination(@PathVariable Long destId) {
        // Use the flat destinationId field
        return ResponseEntity.ok(packageRepository.findByDestinationId(destId));
    }
    
    @GetMapping("/{id}")
    public ResponseEntity<TravelPackage> getPackageById(@PathVariable Long id) {
        // Use the numeric packageId (need to add findByPackageId to repo if not there)
        return packageRepository.findByPackageId(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
}
