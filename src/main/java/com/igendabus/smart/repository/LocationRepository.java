package com.igendabus.smart.repository;

import com.igendabus.smart.model.ELocationType;
import com.igendabus.smart.model.Location;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface LocationRepository extends JpaRepository<Location, UUID> {

    // existsBy: checks if a location with this code already exists
    // Spring auto-generates the SQL: SELECT COUNT(*) WHERE code = ?
    Boolean existsByCode(String code);

    // Find a location by its name and type
    Optional<Location> findByNameAndType(String name, ELocationType type);

    // Find a location by its code and type
    Optional<Location> findByCodeAndType(String code, ELocationType type);

    // Retrieve all users' villages that belong to a given province
    // This traverses: village → cell → sector → district → province
    @Query("SELECT l FROM Location l WHERE l.type = 'VILLAGE' " +
           "AND l.parent.parent.parent.parent.code = :provinceCode")
    List<Location> findVillagesByProvinceCode(@Param("provinceCode") String provinceCode);

    @Query("SELECT l FROM Location l WHERE l.type = 'VILLAGE' " +
           "AND l.parent.parent.parent.parent.name = :provinceName")
    List<Location> findVillagesByProvinceName(@Param("provinceName") String provinceName);
}