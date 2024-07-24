package com.ecobank.soole.services;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ecobank.soole.models.Account;
import com.ecobank.soole.models.Booking;
import com.ecobank.soole.repositories.AccountRepository;
import com.ecobank.soole.repositories.BookingRepository;
import com.ecobank.soole.util.constants.BookingTimeFrameUtil;

@Service
public class BookingService {

    @Autowired
    private BookingRepository bookingRepository;

    @Autowired
    private AccountService accountService;

    public Booking save(Booking booking) {
        if (booking.getId() == null) {
            booking.setCreatedAt(LocalDateTime.now());
        }
        if (booking.getCreatedAt() == null) {
            booking.setCreatedAt(LocalDateTime.now());
        }
        return bookingRepository.save(booking);

    }

    public List<Booking> findAll() {
        return bookingRepository.findAll();
    }

    public List<Booking> findByBusIdAndDate(Long id,LocalDate date) {
        return bookingRepository.findByBusIdAndDate(id, date);
    }

    // public Optional<Booking> findBookingIdAndBusId(Long bookId, Long busId){
    // return bookingRepository.findByBookingIdAndBusBusId(bookId, busId);
    // }

    public Optional<Booking> findByBookingId(Long bookId){
        return bookingRepository.findById(bookId);
    }

    public boolean hasExistingBooking(Long userId, Long busId, LocalDateTime dateTime, boolean isSpecial) {
        boolean isMorning = BookingTimeFrameUtil.isMorning(dateTime);
        boolean isEvening = BookingTimeFrameUtil.isEvening(dateTime);

        List<Booking> existingBookings = bookingRepository.findByAccountIdAndBusBusId(userId, busId);

        Optional<Account> existingAccount = accountService.findById(userId);
        Account account = existingAccount.get();

        for(Booking bookings:existingBookings){
            if (bookings.getBooker().equals(account.getFullName())) {
                return true;
            }
        }
        // specials can book without time constraint
        if (isSpecial) {
            return false;
        }

        // check normal user hasnt booked before
        for(Booking bookings:existingBookings){
            if (bookings.getBooker().equals(account.getFullName())) {
                return true;
            }
        }
        // A normal user still must book at the set time
            if (isEvening && BookingTimeFrameUtil.isEvening(LocalDateTime.now())) {
                return false; // Already booked in the morning
            }
            if (isMorning && BookingTimeFrameUtil.isMorning(LocalDateTime.now())) {
                return false; // Already booked in the evening
            }

            
        

        return true; // No existing booking that violates the rules
    }
}