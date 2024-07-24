package com.ecobank.soole.services.busimplement;

import org.springframework.stereotype.Service;

import com.ecobank.soole.mapper.BusMapper;
import com.ecobank.soole.models.Bus;
import com.ecobank.soole.payload.bus.CreateRouteDTO;
import com.ecobank.soole.repositories.BusRepository;
import com.ecobank.soole.services.RouteService;
import com.ecobank.soole.util.constants.ResourceNotFoundException;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class RouteServiceImpl implements RouteService {
    private final BusRepository busRepository;

    @Override
    public void updateRoute(CreateRouteDTO createRouteDTO, String busId) {
        // Get bus
        Bus bus = busRepository.findById(Long.valueOf(busId)).orElseThrow(() -> new ResourceNotFoundException("Bus not found with Id: " + busId));
        // Update route
        bus = BusMapper.MapRouteToBus(createRouteDTO, bus);
        // Save route
        try {
            busRepository.save(bus);
        } catch (Exception e) {
            throw new RuntimeException("Unable to update route information " + e.getMessage());
        }
    }
}
