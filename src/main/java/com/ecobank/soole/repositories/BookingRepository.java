package com.ecobank.soole.repositories;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.ecobank.soole.models.Booking;

public interface BookingRepository extends JpaRepository<Booking, Long> {
    // List<Booking> findAllByAccountId(Long id);
    List<Booking> findByBusBusIdAndCreatedAt(Long id, LocalDateTime createdAt);
    List<Booking> findByAccountIdAndBusBusId(Long accountId, Long busId);
    List<Booking> findByCreatedAtBefore(LocalDateTime dateTime);
    // Optional<Booking> findByBookingIdAndBusBusId(Long bookId, Long busId);
    @Query("SELECT b FROM Booking b WHERE b.bus.id = :busId AND DATE(b.createdAt) = :date")
    List<Booking> findByBusIdAndDate(@Param("busId") Long busId, @Param("date") LocalDate date);
}
