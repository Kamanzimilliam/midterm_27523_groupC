package com.igendabus.smart.service;

import com.igendabus.smart.model.BusRoute;
import com.igendabus.smart.repository.BusRouteRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class BusRouteService {

    @Autowired
    private BusRouteRepository busRouteRepository;

    // Save a new route
    public String saveRoute(BusRoute busRoute) {
        if (busRouteRepository.existsByRouteCode(busRoute.getRouteCode())) {
            return "Route with code " + busRoute.getRouteCode() + " already exists";
        }
        busRouteRepository.save(busRoute);
        return "Bus route saved successfully";
    }

    // Get all routes with pagination + sorting
    public Page<BusRoute> getAllRoutes(int page, int size, String sortBy) {
        Pageable pageable = PageRequest.of(page, size, Sort.by(sortBy).ascending());
        return busRouteRepository.findAll(pageable);
    }

    // Get single route by ID
    public BusRoute getRouteById(UUID id) {
        return busRouteRepository.findById(id).orElse(null);
    }
}