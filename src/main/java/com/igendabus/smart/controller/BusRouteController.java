package com.igendabus.smart.controller;

import com.igendabus.smart.model.BusRoute;
import com.igendabus.smart.service.BusRouteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/routes")
public class BusRouteController {

    @Autowired
    private BusRouteService busRouteService;

    // Save a new bus route
    // POST http://localhost:8080/api/routes/save
    @PostMapping("/save")
    public ResponseEntity<?> saveRoute(@RequestBody BusRoute busRoute) {
        String result = busRouteService.saveRoute(busRoute);
        if (result.equals("Bus route saved successfully")) {
            return new ResponseEntity<>(result, HttpStatus.CREATED);
        }
        return new ResponseEntity<>(result, HttpStatus.CONFLICT);
    }

    // Get all routes with pagination + sorting
    // GET http://localhost:8080/api/routes/all?page=0&size=10&sortBy=routeName
    @GetMapping("/all")
    public ResponseEntity<Page<BusRoute>> getAllRoutes(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "routeName") String sortBy) {
        return new ResponseEntity<>(
            busRouteService.getAllRoutes(page, size, sortBy), HttpStatus.OK);
    }

    // Get single route by ID
    // GET http://localhost:8080/api/routes/{id}
    @GetMapping("/{id}")
    public ResponseEntity<?> getRouteById(@PathVariable UUID id) {
        BusRoute route = busRouteService.getRouteById(id);
        if (route == null) return new ResponseEntity<>("Route not found", HttpStatus.NOT_FOUND);
        return new ResponseEntity<>(route, HttpStatus.OK);
    }
}