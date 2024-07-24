package com.ecobank.soole.controller;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.ecobank.soole.models.Account;
import com.ecobank.soole.models.Booking;
import com.ecobank.soole.models.Bus;
import com.ecobank.soole.payload.booking.BookingPayloadDTO;
import com.ecobank.soole.payload.booking.BookingViewDTO;
import com.ecobank.soole.payload.booking.BookingViewListDTO;
import com.ecobank.soole.repositories.AccountRepository;
import com.ecobank.soole.repositories.BusRepository;
import com.ecobank.soole.services.BookingService;
import com.ecobank.soole.services.BusService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping("/api/v1/booking")
@Slf4j
@Tag(name = "Booking Controller", description = "Controller for Booking management")
public class BookingController {

    @Autowired
    private BookingService bookingService;

    @Autowired
    private AccountRepository accountRepository;

    @Autowired
    private BusRepository busRepository;

    @Autowired
    private BusService busService;

    @PostMapping(value = "/bookseat", produces = "application/json")
    @ResponseStatus(HttpStatus.CREATED)
    @ApiResponse(responseCode = "400", description = "token")
    @ApiResponse(responseCode = "201", description = "Account added")
    @ApiResponse(responseCode = "200", description = "success")
    @Operation(summary = "Reserve a spot in the chosen bus")
    @SecurityRequirement(name = "soole-demo-api")
    public ResponseEntity<BookingViewDTO> reserveSpot(@ModelAttribute BookingPayloadDTO bookingDTO,
            Authentication authentication) {
        Booking booking = new Booking();
        Optional<Bus> optionalBus = busRepository.findById(bookingDTO.getBusId());
        Bus bus = optionalBus.get();
        if (!optionalBus.isPresent()) {
            return new ResponseEntity<>(new BookingViewDTO(), HttpStatus.BAD_REQUEST);
        }
        Optional<Account> optionalAccount = accountRepository.findById(bookingDTO.getUserId());
        Account accountVerified = optionalAccount.get();
        Account account = optionalAccount.get();
        if (!optionalAccount.isPresent()) {
            return new ResponseEntity<>(new BookingViewDTO(), HttpStatus.BAD_REQUEST);
        }

        LocalDateTime now = LocalDateTime.now();
        boolean isSpecial = "SPECIAL".equals(account.getSpecial());
        if (bookingService.hasExistingBooking(account.getId(), bus.getBusId(), now, isSpecial)) {
            BookingViewDTO existingBookingViewDTO = new BookingViewDTO();

            List<Booking> optionalBooking = bookingService.findByBusIdAndDate(bookingDTO.getBusId(), LocalDate.now());
            for (Booking existingBookings : optionalBooking) {
                if (existingBookings.getBooker().equals(account.getFullName())) {
                    existingBookingViewDTO.setCreatedAt(existingBookings.getCreatedAt());
                    System.out.println(existingBookings.getBooker());
                    existingBookingViewDTO.setTake_off_point(existingBookings.getTake_off_point());
                    existingBookingViewDTO.setDrop_off_point(existingBookings.getDrop_off_point());
                    existingBookingViewDTO.setStatus(existingBookings.getStatus());
                    existingBookingViewDTO.setRoute(existingBookings.getRoute());
                    existingBookingViewDTO.setDrop_off_point(existingBookings.getDrop_off_point());
                    existingBookingViewDTO.setTime_of_departure(existingBookings.getTime_of_departure());
                    System.out.println(existingBookings);
                    existingBookingViewDTO.setBoard(existingBookings.getBoard());
                }
                return new ResponseEntity<>(existingBookingViewDTO, HttpStatus.OK);
            }
        }
        List<Booking> allSeats = bookingService.findAll();
        int totalSeats = allSeats.size();

        if (accountVerified.getVerified().equals("PENDING") || accountVerified.getVerified().equals("REJECTED")) {
            return new ResponseEntity<>(new BookingViewDTO(), HttpStatus.BAD_REQUEST);
        }

        booking.setAccount(account);
        booking.setBooker(account.getFullName());
        booking.setCreatedAt(now);
        booking.setDrop_off_point(bookingDTO.getDrop_off_point());
        booking.setRoute(bookingDTO.getRoute());
        booking.setAccount(account);
        booking.setBus(bus);
        booking.setSpecial(account.getSpecial());
        booking.setBooker(account.getFullName());

        if (totalSeats < bus.getBusCapacity()) {
            booking.setStatus("RESERVED");
        } else {
            booking.setStatus("WAITLIST");
        }
        booking.setTake_off_point("EPAC");
        booking.setTime_of_departure(LocalDateTime.now());
        booking.setBoard("PENDING");
        bookingService.save(booking);

        System.out.println(bookingDTO);
        BookingViewDTO bookingViewDTO = new BookingViewDTO();
        bookingViewDTO.setCreatedAt(LocalDateTime.now());
        bookingViewDTO.setTake_off_point("EPAC");
        bookingViewDTO.setDrop_off_point(booking.getDrop_off_point());
        bookingViewDTO.setStatus(booking.getStatus());
        bookingViewDTO.setRoute(bookingDTO.getRoute());
        bookingViewDTO.setDrop_off_point(booking.getDrop_off_point());
        bookingViewDTO.setTime_of_departure(booking.getTime_of_departure());
        bookingViewDTO.setBoard("PENDING");
        

        return new ResponseEntity<>(bookingViewDTO, HttpStatus.CREATED);

        // return ResponseEntity.ok("Seat Booked successfully");
    }

    @GetMapping("/{busId}/bookings")
    @ApiResponse(responseCode = "200", description = "List of users")
    @ApiResponse(responseCode = "401", description = "Token missing")
    @ApiResponse(responseCode = "403", description = "Token error")
    @Operation(summary = "List of today's bookings")
    @SecurityRequirement(name = "soole-demo-api")
    public List<BookingViewListDTO> busBookings(@PathVariable Long busId) {
        Optional<Bus> optionalBus = busService.fetchById(busId);
        Bus bus;
        if (!optionalBus.isPresent()) {
            bus=optionalBus.get();
        }
        List<BookingViewListDTO> busBookings = new ArrayList<>();
        LocalDate today = LocalDate.now();
        for(Booking bookings: bookingService.findByBusIdAndDate(busId, today)){
            busBookings.add(new BookingViewListDTO(
                bookings.getTime_of_departure(),
                bookings.getCreatedAt(),
                bookings.getTake_off_point(),
                bookings.getDrop_off_point(),
                bookings.getStatus(),
                bookings.getRoute(),
                bookings.getBoard()
            ));
        };
        System.out.println(busBookings);
        return busBookings;
    }

    @PutMapping(value = "/{bookingId}/update")
    @ApiResponse(responseCode = "200", description = "USER BOARDED SUCCESSFULLY")
    @ApiResponse(responseCode = "401", description = "Token missing")
    @ApiResponse(responseCode = "403", description = "Token error")
    @Operation(summary = "Update booking")
    @SecurityRequirement(name = "soole-demo-api")
    public ResponseEntity<String> updateBooking(@PathVariable Long bookingId) {
        Optional<Booking> optionalBooking = bookingService.findByBookingId(bookingId);
        if (!optionalBooking.isPresent()) {
            return new ResponseEntity<String>("booking does not exist", HttpStatus.BAD_REQUEST);
        }
        Booking foundBooking = optionalBooking.get();
        foundBooking.setBoard("BOARDED SUCCESSFULLY");
        bookingService.save(foundBooking);
        return new ResponseEntity<String>("USER BOARDED SUCCESSFULLY", HttpStatus.OK);
    }
}

