package com.igendabus.smart.controller;

import com.igendabus.smart.model.Location;
import com.igendabus.smart.service.LocationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/locations")
public class LocationController {

    @Autowired
    private LocationService locationService;

    @PostMapping("/save")
    public ResponseEntity<?> saveLocation(@RequestBody Location location) {
        return locationService.saveLocation(location);
    }

    @PostMapping("/save/child")
    public ResponseEntity<?> saveChildLocation(
            @RequestBody Location location,
            @RequestParam String parentId) {
        return locationService.saveLocationWithParent(location, parentId);
    }

    @GetMapping("/all")
    public ResponseEntity<List<Location>> getAllLocations() {
        return new ResponseEntity<>(locationService.getAllLocations(), HttpStatus.OK);
    }
}