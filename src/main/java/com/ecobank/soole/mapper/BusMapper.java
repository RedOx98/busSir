package com.ecobank.soole.mapper;

import com.ecobank.soole.models.Bus;
import com.ecobank.soole.models.BusStop;
import com.ecobank.soole.payload.bus.CreateBusDTO;
import com.ecobank.soole.payload.bus.CreateBusStopDTO;
import com.ecobank.soole.payload.bus.CreateRouteDTO;

public final class BusMapper {
    // Prevent instantiation
    private BusMapper() {
    }

    // Create Bus stop
    public static BusStop MapToBusStop(CreateBusStopDTO createBusStopDTO, BusStop busStop, Bus bus) {
        busStop.setBus(bus);
        busStop.setBusStopName(createBusStopDTO.getBusStopName());
        return busStop;
    }

    // Create | Update bus
    public static Bus MapToBus(CreateBusDTO createBusDTO, Bus bus) {
        bus.setBusCapacity(createBusDTO.getBusCapacity());
        bus.setBusModel(createBusDTO.getBusModel());
        bus.setBusColor(createBusDTO.getBusColor());
        bus.setBusNumber(createBusDTO.getBusNumber());
        bus.setOperationalStatus(createBusDTO.getOperationalStatus());
        return bus;
    }

    // Create | Update route
    public static Bus MapRouteToBus(CreateRouteDTO createRouteDTO, Bus bus) {
        bus.setRouteName(createRouteDTO.getRouteName());
        bus.setDriverId(createRouteDTO.getDriverId());
        bus.setCaptainId(createRouteDTO.getCaptainId());
        return bus;
    }
}
