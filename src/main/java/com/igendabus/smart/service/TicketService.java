package com.igendabus.smart.service;

import com.igendabus.smart.model.*;
import com.igendabus.smart.repository.BusRouteRepository;
import com.igendabus.smart.repository.TicketRepository;
import com.igendabus.smart.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.UUID;

@Service
public class TicketService {

    @Autowired
    private TicketRepository ticketRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private BusRouteRepository busRouteRepository;

    // Save ticket with user + routes + auto-generate QR code
    public String saveTicket(UUID userId, List<UUID> routeIds) {

        // Check user exists
        User user = userRepository.findById(userId).orElse(null);
        if (user == null) return "User not found";

        // existsBy: prevent duplicate ticket numbers
        String ticketNumber = "TKT-" + System.currentTimeMillis();
        if (ticketRepository.existsByTicketNumber(ticketNumber)) {
            return "Ticket number already exists, try again";
        }

        // Fetch all routes for Many-to-Many
        Set<BusRoute> routes = new HashSet<>();
        double totalFare = 0;
        for (UUID routeId : routeIds) {
            BusRoute route = busRouteRepository.findById(routeId).orElse(null);
            if (route != null) {
                routes.add(route);
                totalFare += route.getBaseFare();
            }
        }

        // Build the ticket
        Ticket ticket = new Ticket();
        ticket.setTicketNumber(ticketNumber);
        ticket.setPurchasedAt(LocalDateTime.now());
        ticket.setStatus(ETicketStatus.ACTIVE);
        ticket.setAmountPaid(totalFare);
        ticket.setUser(user);
        ticket.setRoutes(routes);

        // One-to-One: auto-generate QR code for this ticket
        QRCode qrCode = new QRCode();
        qrCode.setQrToken(UUID.randomUUID().toString());
        qrCode.setGeneratedAt(LocalDateTime.now());
        qrCode.setExpiresAt(LocalDateTime.now().plusHours(24));
        qrCode.setIsScanned(false);
        qrCode.setTicket(ticket);

        ticket.setQrCode(qrCode);
        ticketRepository.save(ticket);
        return "Ticket saved successfully. QR Code: " + qrCode.getQrToken();
    }

    // Get all tickets with pagination + sorting
    public Page<Ticket> getAllTickets(int page, int size, String sortBy) {
        Pageable pageable = PageRequest.of(page, size, Sort.by(sortBy).ascending());
        return ticketRepository.findAll(pageable);
    }

    // Get tickets by user with pagination
    public Page<Ticket> getTicketsByUser(UUID userId, int page, int size) {
        Pageable pageable = PageRequest.of(page, size, Sort.by("purchasedAt").descending());
        return ticketRepository.findByUserId(userId, pageable);
    }
}