package com.ecobank.soole.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.ecobank.soole.models.BusStop;

@Repository
public interface BusStopRepository extends JpaRepository<BusStop, Long> {

}