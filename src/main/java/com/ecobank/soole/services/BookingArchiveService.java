package com.ecobank.soole.services;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ecobank.soole.models.Booking;
import com.ecobank.soole.models.BookingArchive;
import com.ecobank.soole.repositories.BookingArchiveRepository;
import com.ecobank.soole.repositories.BookingRepository;

@Service
public class BookingArchiveService {
    @Autowired
    private BookingArchiveRepository archiveRepository;

    @Autowired
    private BookingRepository bookingRepository;

    @Scheduled(cron = "0 0 0 * * ?") //everyday at midnight
    @Transactional
    public void archiveOldBookings(){
        // Define the period after which bookings should be archived
        LocalDateTime threshold = LocalDateTime.now().minusDays(30);

        // Fetch Old Bookings
        List<Booking> oldBookings = bookingRepository.findByCreatedAtBefore(threshold);
        for(Booking booking: oldBookings){
            BookingArchive archive = new BookingArchive();
            archive.setTime_of_departure(booking.getTime_of_departure());
            archive.setCreatedAt(booking.getCreatedAt());
            archive.setTake_off_point(booking.getTake_off_point());
            archive.setDrop_off_point(booking.getDrop_off_point());
            archive.setStatus(booking.getStatus());
            archive.setRoute(booking.getRoute());
            archive.setBoard(booking.getBoard());
            archive.setBus_id(booking.getBus().getBusId());
            archive.setAccount_id(booking.getAccount().getId());

            archiveRepository.save(archive);
        }
        // Delete old bookings from original table
        bookingRepository.deleteAll(oldBookings);
    }
}
