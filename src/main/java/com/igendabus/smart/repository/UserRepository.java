package com.igendabus.smart.repository;

import com.igendabus.smart.model.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface UserRepository extends JpaRepository<User, UUID> {

    // existsBy: checks if email is already registered
    Boolean existsByEmail(String email);

    // existsBy: checks if phone number is already registered
    Boolean existsByPhoneNumber(String phoneNumber);

    // Pagination + Sorting built-in via Pageable parameter
    // Spring generates: SELECT * FROM app_user LIMIT ? OFFSET ?
    Page<User> findAll(Pageable pageable);

    // Retrieve all users from a province using province CODE
    // Traverses: user.village → cell → sector → district → province
    @Query("SELECT u FROM User u WHERE " +
           "u.village.parent.parent.parent.parent.code = :provinceCode")
    Page<User> findUsersByProvinceCode(@Param("provinceCode") String provinceCode, Pageable pageable);

    // Retrieve all users from a province using province NAME
    @Query("SELECT u FROM User u WHERE " +
           "u.village.parent.parent.parent.parent.name = :provinceName")
    Page<User> findUsersByProvinceName(@Param("provinceName") String provinceName, Pageable pageable);

    // Find users by province using ONE identifier (code OR name)
    // Works with both: ?identifier=KG or ?identifier=Kigali
    @Query("SELECT u FROM User u WHERE " +
           "u.village.parent.parent.parent.parent.code = :identifier OR " +
           "u.village.parent.parent.parent.parent.name = :identifier")
    Page<User> findUsersByProvinceIdentifier(@Param("identifier") String identifier, Pageable pageable);

    // Find users by ANY location level (village, cell, sector, district, province)
    // Works with name or code at any level
    @Query("SELECT u FROM User u WHERE " +
           "u.village.name = :identifier OR " +
           "u.village.code = :identifier OR " +
           "u.village.parent.name = :identifier OR " +
           "u.village.parent.code = :identifier OR " +
           "u.village.parent.parent.name = :identifier OR " +
           "u.village.parent.parent.code = :identifier OR " +
           "u.village.parent.parent.parent.name = :identifier OR " +
           "u.village.parent.parent.parent.code = :identifier OR " +
           "u.village.parent.parent.parent.parent.name = :identifier OR " +
           "u.village.parent.parent.parent.parent.code = :identifier")
    Page<User> findUsersByAnyLocation(@Param("identifier") String identifier, Pageable pageable);
}