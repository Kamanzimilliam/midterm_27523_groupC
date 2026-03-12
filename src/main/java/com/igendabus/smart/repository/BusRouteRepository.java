package com.igendabus.smart.repository;

import com.igendabus.smart.model.BusRoute;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface BusRouteRepository extends JpaRepository<BusRoute, UUID> {

    // existsBy: prevents duplicate route codes
    Boolean existsByRouteCode(String routeCode);

    // Pagination + Sorting on routes
    Page<BusRoute> findAll(Pageable pageable);
}