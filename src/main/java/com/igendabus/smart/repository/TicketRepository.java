package com.igendabus.smart.repository;

import com.igendabus.smart.model.Ticket;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface TicketRepository extends JpaRepository<Ticket, UUID> {

    // existsBy: prevents duplicate ticket numbers
    Boolean existsByTicketNumber(String ticketNumber);

    // Get all tickets for a specific user with pagination
    Page<Ticket> findByUserId(UUID userId, Pageable pageable);

    // Pagination + Sorting on all tickets
    Page<Ticket> findAll(Pageable pageable);
}