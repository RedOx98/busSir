package com.ecobank.soole.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Entity
@Getter
@Setter
@ToString
@Table(name = "bus_stops")
public class BusStop extends BaseEntity {
    // Primary key
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "bus_stop_id")
    private Long busStopId;

    @Column(name = "name")
    private String busStopName;

    @ManyToOne
    @JoinColumn(name = "bus_id", nullable = false)
    @JsonIgnore
    private Bus bus;
}
