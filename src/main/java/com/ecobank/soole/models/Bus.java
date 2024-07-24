package com.ecobank.soole.models;

import java.util.List;
import java.util.Set;

import com.ecobank.soole.util.constants.BusEnum;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
public class Bus extends BaseEntity{
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "bus_id")
    private Long busId;

    @Column(name = "bus_number", nullable = false, unique = true)
    private String busNumber;

    @Enumerated(EnumType.STRING)
    @Column(name = "operational_status", columnDefinition = "enum('INACTIVE','ACTIVE') DEFAULT 'ACTIVE'")
    private BusEnum.OperationalStatus operationalStatus;

    @Column(name = "bus_model", nullable = false)
    private String busModel;

    @Column(name = "bus_capacity", nullable = false)
    private int busCapacity;

    @Column(name = "bus_color", nullable = false)
    private String busColor;

    @Column(name = "route_name")
    private String routeName;

    @Column(name = "driver_id")
    private String driverId;

    @Column(name = "captain_id")
    private String captainId;

    @OneToMany(mappedBy = "bus")
    private Set<BusStop> busStops;

    @OneToMany(mappedBy = "bus", cascade = CascadeType.ALL)
    private List<Account> accounts;
}
