package com.ecobank.soole.services;

import com.ecobank.soole.payload.bus.CreateBusStopDTO;

public interface BusStopService {
    void createBusStop(CreateBusStopDTO createBusStopDTO, String busStopId);

    void updateBusStop(CreateBusStopDTO createBusStopDTO, String busStopId);

    void deleteBusStop(String busStopId);
}
