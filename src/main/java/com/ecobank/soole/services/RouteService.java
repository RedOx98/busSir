package com.ecobank.soole.services;

import com.ecobank.soole.payload.bus.CreateRouteDTO;

public interface RouteService {
//    void createRoute(CreateRouteDTO createRouteDTO, String busId);

    void updateRoute(CreateRouteDTO createRouteDTO, String busId);
}
