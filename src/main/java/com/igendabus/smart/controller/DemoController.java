package com.igendabus.smart.controller;

import com.igendabus.smart.model.User;
import com.igendabus.smart.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/demo")
public class DemoController {

    @Autowired
    private UserRepository userRepository;

    // GET /api/demo/users/paged?page=0&size=2&sortBy=firstName
    // This endpoint explains pagination visually
    @GetMapping("/users/paged")
    public ResponseEntity<Map<String, Object>> getPagedUsersWithExplanation(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "2") int size,
            @RequestParam(defaultValue = "firstName") String sortBy) {

        Pageable pageable = PageRequest.of(page, size, Sort.by(sortBy).ascending());
        Page<User> userPage = userRepository.findAll(pageable);

        // Build detailed explanation response
        Map<String, Object> response = new HashMap<>();
        response.put("explanation", "Pagination splits large datasets into pages for better performance");
        response.put("currentPage", userPage.getNumber());
        response.put("pageSize", userPage.getSize());
        response.put("totalUsers", userPage.getTotalElements());
        response.put("totalPages", userPage.getTotalPages());
        response.put("isFirstPage", userPage.isFirst());
        response.put("isLastPage", userPage.isLast());
        response.put("sortedBy", sortBy);
        response.put("users", userPage.getContent());

        return new ResponseEntity<>(response, HttpStatus.OK);
    }
}
