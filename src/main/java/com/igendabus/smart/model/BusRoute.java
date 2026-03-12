package com.igendabus.smart.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "bus_route")
public class BusRoute {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(nullable = false, unique = true)
    private String routeCode;

    @Column(nullable = false)
    private String routeName;

    @Column(nullable = false)
    private String origin;

    @Column(nullable = false)
    private String destination;

    private Double distanceKm;

    @Column(nullable = false)
    private Double baseFare;
}