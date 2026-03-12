package com.igendabus.smart.service;

import com.igendabus.smart.model.ELocationType;
import com.igendabus.smart.model.Location;
import com.igendabus.smart.repository.LocationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class LocationService {

    @Autowired
    private LocationRepository locationRepository;

    // Returns the saved Location object so Postman shows the ID
    public ResponseEntity<?> saveLocation(Location location) {
        if (locationRepository.existsByCode(location.getCode())) {
            return new ResponseEntity<>(
                "Location with code " + location.getCode() + " already exists",
                HttpStatus.CONFLICT);
        }
        Location saved = locationRepository.save(location);
        return new ResponseEntity<>(saved, HttpStatus.CREATED);
    }

    // Save child with parent — returns saved object with ID
    public ResponseEntity<?> saveLocationWithParent(Location location, String parentId) {
        if (locationRepository.existsByCode(location.getCode())) {
            return new ResponseEntity<>(
                "Location with code " + location.getCode() + " already exists",
                HttpStatus.CONFLICT);
        }
        if (parentId != null) {
            Location parent = locationRepository.findById(UUID.fromString(parentId))
                    .orElse(null);
            if (parent == null) {
                return new ResponseEntity<>("Parent location not found", HttpStatus.NOT_FOUND);
            }
            location.setParent(parent);
        }
        Location saved = locationRepository.save(location);
        return new ResponseEntity<>(saved, HttpStatus.CREATED);
    }

    // Get all locations
    public List<Location> getAllLocations() {
        return locationRepository.findAll();
    }

    // Find village by name
    public Location findVillageByName(String name) {
        return locationRepository
                .findByNameAndType(name, ELocationType.VILLAGE)
                .orElse(null);
    }

    // Find village by code
    public Location findVillageByCode(String code) {
        return locationRepository
                .findByCodeAndType(code, ELocationType.VILLAGE)
                .orElse(null);
    }
}