package com.igendabus.smart.controller;

import com.igendabus.smart.model.Ticket;
import com.igendabus.smart.service.TicketService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/tickets")
public class TicketController {

    @Autowired
    private TicketService ticketService;

    // Buy a ticket — pass userId and list of routeIds
    // POST http://localhost:8080/api/tickets/buy?userId=UUID&routeIds=UUID1,UUID2
    @PostMapping("/buy")
    public ResponseEntity<?> buyTicket(
            @RequestParam UUID userId,
            @RequestParam List<UUID> routeIds) {
        String result = ticketService.saveTicket(userId, routeIds);
        if (result.startsWith("Ticket saved successfully")) {
            return new ResponseEntity<>(result, HttpStatus.CREATED);
        }
        return new ResponseEntity<>(result, HttpStatus.BAD_REQUEST);
    }

    // Get all tickets with pagination + sorting
    // GET http://localhost:8080/api/tickets/all?page=0&size=10&sortBy=purchasedAt
    @GetMapping("/all")
    public ResponseEntity<Page<Ticket>> getAllTickets(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "purchasedAt") String sortBy) {
        return new ResponseEntity<>(
            ticketService.getAllTickets(page, size, sortBy), HttpStatus.OK);
    }

    // Get tickets by user
    // GET http://localhost:8080/api/tickets/user/{userId}?page=0&size=10
    @GetMapping("/user/{userId}")
    public ResponseEntity<Page<Ticket>> getTicketsByUser(
            @PathVariable UUID userId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        return new ResponseEntity<>(
            ticketService.getTicketsByUser(userId, page, size), HttpStatus.OK);
    }
}