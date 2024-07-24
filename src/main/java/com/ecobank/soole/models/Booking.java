package com.ecobank.soole.models;

import java.time.LocalDateTime;

import com.ecobank.soole.util.constants.BookingEnum;
import com.ecobank.soole.util.constants.BusEnum;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
@Table(name = "booking")
public class Booking {
    
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Long id;

    private LocalDateTime time_of_departure;

    private LocalDateTime createdAt;

    private String take_off_point;

    private String drop_off_point;

    // @Enumerated(EnumType.STRING)
    // @Column(name = "status", columnDefinition = "enum('RESERVED','WAITLIST') DEFAULT 'RESERVED'")
    private String status;

    private String route;

    private String booker;

    private String special;

    private String board;

    @ManyToOne
    @JoinColumn(name = "bus_id", referencedColumnName = "bus_id", nullable = true)
    private Bus bus;

    @ManyToOne
    @JoinColumn(name = "account_id", referencedColumnName = "id", nullable = true)
    private Account account;
}
