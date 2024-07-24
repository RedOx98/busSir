package com.ecobank.soole.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.ecobank.soole.models.BookingArchive;

public interface BookingArchiveRepository extends JpaRepository<BookingArchive, Long> {
    
}
