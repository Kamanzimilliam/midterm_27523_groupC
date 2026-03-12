package com.igendabus.smart.controller;

import com.igendabus.smart.model.User;
import com.igendabus.smart.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/users")
public class UserController {

    @Autowired
    private UserService userService;

    // Save a new user — only needs village name or code
    // POST http://localhost:8080/api/users/save?villageNameOrCode=Amahoro
    @PostMapping("/save")
    public ResponseEntity<?> saveUser(
            @RequestBody User user,
            @RequestParam String villageNameOrCode) {
        String result = userService.saveUser(user, villageNameOrCode);
        if (result.equals("User saved successfully")) {
            return new ResponseEntity<>(result, HttpStatus.CREATED);
        }
        return new ResponseEntity<>(result, HttpStatus.CONFLICT);
    }

    // Get all users with pagination + sorting
    // GET http://localhost:8080/api/users/all?page=0&size=10&sortBy=firstName
    @GetMapping("/all")
    public ResponseEntity<Page<User>> getAllUsers(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "firstName") String sortBy) {
        return new ResponseEntity<>(userService.getAllUsers(page, size, sortBy), HttpStatus.OK);
    }

    // Get single user by ID
    // GET http://localhost:8080/api/users/{id}
    @GetMapping("/{id}")
    public ResponseEntity<?> getUserById(@PathVariable UUID id) {
        User user = userService.getUserById(id);
        if (user == null) return new ResponseEntity<>("User not found", HttpStatus.NOT_FOUND);
        return new ResponseEntity<>(user, HttpStatus.OK);
    }

    // Get users by province CODE
    // GET http://localhost:8080/api/users/province/code?provinceCode=KG
    @GetMapping("/province/code")
    public ResponseEntity<Page<User>> getUsersByProvinceCode(
            @RequestParam String provinceCode,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        return new ResponseEntity<>(
            userService.getUsersByProvinceCode(provinceCode, page, size), HttpStatus.OK);
    }

    // Get users by province NAME
    // GET http://localhost:8080/api/users/province/name?provinceName=Kigali
    @GetMapping("/province/name")
    public ResponseEntity<Page<User>> getUsersByProvinceName(
            @RequestParam String provinceName,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        return new ResponseEntity<>(
            userService.getUsersByProvinceName(provinceName, page, size), HttpStatus.OK);
    }
// GET /api/users/by-province?identifier=Kigali OR ?identifier=KG
@GetMapping("/by-province")
public ResponseEntity<Page<User>> getUsersByProvince(
        @RequestParam String identifier,
        @RequestParam(defaultValue = "0") int page,
        @RequestParam(defaultValue = "10") int size) {
    return new ResponseEntity<>(
        userService.getUsersByProvinceIdentifier(identifier, page, size), HttpStatus.OK);
}

// GET /api/users/by-location?identifier=Gasabo (any level)
@GetMapping("/by-location")
public ResponseEntity<Page<User>> getUsersByLocation(
        @RequestParam String identifier,
        @RequestParam(defaultValue = "0") int page,
        @RequestParam(defaultValue = "10") int size) {
    return new ResponseEntity<>(
        userService.getUsersByAnyLocation(identifier, page, size), HttpStatus.OK);
}

// GET /api/users/check-exists?email=jean.mugisha@gmail.com
@GetMapping("/check-exists")
public ResponseEntity<?> checkUserExists(@RequestParam String email) {
    boolean exists = userService.checkUserExists(email);
    if (exists) {
        return new ResponseEntity<>("User with email " + email + " EXISTS", HttpStatus.OK);
    }
    return new ResponseEntity<>("User with email " + email + " does NOT exist", HttpStatus.NOT_FOUND);
}
}