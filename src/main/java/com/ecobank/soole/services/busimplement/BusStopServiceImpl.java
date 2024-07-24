package com.ecobank.soole.services.busimplement;

import org.springframework.stereotype.Service;

import com.ecobank.soole.mapper.BusMapper;
import com.ecobank.soole.models.Bus;
import com.ecobank.soole.models.BusStop;
import com.ecobank.soole.payload.bus.CreateBusStopDTO;
import com.ecobank.soole.repositories.BusRepository;
import com.ecobank.soole.repositories.BusStopRepository;
import com.ecobank.soole.services.BusStopService;
import com.ecobank.soole.util.constants.ResourceNotFoundException;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class BusStopServiceImpl implements BusStopService {
    private final BusStopRepository busStopRepository;
    private final BusRepository busRepository;

    // Create bus stop
    @Override
    public void createBusStop(CreateBusStopDTO createBusStopDTO, String busId) {
        // Fetch bus
        Bus bus = busRepository.findById(Long.valueOf(busId)).orElseThrow(() -> new ResourceNotFoundException("Bus not found with Id: " + busId));
        BusStop busStop = BusMapper.MapToBusStop(createBusStopDTO, new BusStop(), bus);
        try {
            busStopRepository.save(busStop);
        } catch (Exception e) {
            throw new RuntimeException("Error adding bus stop " + e.getMessage());
        }
    }

    // Update bus stop name
    @Override
    public void updateBusStop(CreateBusStopDTO createBusStopDTO, String busStopId) {
        // Fetch bus stop
        BusStop busStop = busStopRepository.findById(Long.valueOf(busStopId)).orElseThrow(() -> new ResourceNotFoundException("Bus stop not found with Id: " + busStopId));
        busStop.setBusStopName(createBusStopDTO.getBusStopName());
        try {
            busStopRepository.save(busStop);
        } catch (Exception e) {
            busStopRepository.save(busStop);
        }

    }

    // Delete bus stop
    @Override
    public void deleteBusStop(String busStopId) {
        try {
            busStopRepository.deleteById(Long.valueOf(busStopId));
        } catch (Exception e) {
            throw new RuntimeException("Error updating bus stop " + e.getMessage());
        }
    }
}
