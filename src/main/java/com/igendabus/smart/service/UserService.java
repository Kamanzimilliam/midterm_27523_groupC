package com.igendabus.smart.service;

import com.igendabus.smart.model.Location;
import com.igendabus.smart.model.User;
import com.igendabus.smart.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private LocationService locationService;

    // Save user — only needs village name or code
    public String saveUser(User user, String villageNameOrCode) {

        // existsBy checks
        if (userRepository.existsByEmail(user.getEmail())) {
            return "Email already registered";
        }
        if (userRepository.existsByPhoneNumber(user.getPhoneNumber())) {
            return "Phone number already registered";
        }

        // Find the village — because of relationships it links to
        // Cell → Sector → District → Province automatically
        Location village = locationService.findVillageByName(villageNameOrCode);
        if (village == null) {
            village = locationService.findVillageByCode(villageNameOrCode);
        }
        if (village == null) {
            return "Village not found. Please provide a valid village name or code";
        }

        user.setVillage(village);
        userRepository.save(user);
        return "User saved successfully";
    }

    // Get all users with PAGINATION + SORTING
    // Pageable controls: which page, how many per page, sort by which field
    public Page<User> getAllUsers(int page, int size, String sortBy) {
        Pageable pageable = PageRequest.of(page, size, Sort.by(sortBy).ascending());
        return userRepository.findAll(pageable);
    }

    // Get users by PROVINCE CODE
    public Page<User> getUsersByProvinceCode(String provinceCode, int page, int size) {
        Pageable pageable = PageRequest.of(page, size, Sort.by("firstName").ascending());
        return userRepository.findUsersByProvinceCode(provinceCode, pageable);
    }

    // Get users by PROVINCE NAME
    public Page<User> getUsersByProvinceName(String provinceName, int page, int size) {
        Pageable pageable = PageRequest.of(page, size, Sort.by("firstName").ascending());
        return userRepository.findUsersByProvinceName(provinceName, pageable);
    }

    // Get single user by ID
    public User getUserById(UUID id) {
        return userRepository.findById(id).orElse(null);
    }
// Get users by province — works with BOTH code and name
public Page<User> getUsersByProvinceIdentifier(String identifier, int page, int size) {
    Pageable pageable = PageRequest.of(page, size, Sort.by("firstName").ascending());
    return userRepository.findUsersByProvinceIdentifier(identifier, pageable);
}

// Get users by ANY location level
public Page<User> getUsersByAnyLocation(String identifier, int page, int size) {
    Pageable pageable = PageRequest.of(page, size, Sort.by("firstName").ascending());
    return userRepository.findUsersByAnyLocation(identifier, pageable);
}

// Check if user exists by email
public boolean checkUserExists(String email) {
    return userRepository.existsByEmail(email);
}
}