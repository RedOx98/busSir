package com.ecobank.soole.services.busimplement;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.ecobank.soole.mapper.BusMapper;
import com.ecobank.soole.models.Bus;
import com.ecobank.soole.payload.bus.BusFetchRequestDTO;
import com.ecobank.soole.payload.bus.CreateBusDTO;
import com.ecobank.soole.payload.bus.CreateRouteDTO;
import com.ecobank.soole.repositories.BusRepository;
import com.ecobank.soole.services.BusService;
import com.ecobank.soole.util.constants.BusEnum;
import com.ecobank.soole.util.constants.ObjectAlreadyExistsException;
import com.ecobank.soole.util.constants.ResourceNotFoundException;

import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
public class BusServiceImpl implements BusService {
    private final BusRepository busRepository;

    @Override
    public void createBus(CreateBusDTO createBusDTO) {
        // Check if bus with same bus number already exists
        if (busRepository.findByBusNumber(createBusDTO.getBusNumber()).isPresent()) {
            throw new ObjectAlreadyExistsException("Bus with bus number " + createBusDTO.getBusNumber() + " already exists");
        }
        Bus bus = BusMapper.MapToBus(createBusDTO, new Bus());
        try {
            busRepository.save(bus);
        } catch (Exception e) {
            log.error(e.getMessage());
            throw new RuntimeException("Unable to create bus " + e.getMessage());
        }
    }

    @Override
    public void addRouteDetails(CreateRouteDTO createRouteDTO, String busId) {
        // Fetch bus
        Bus bus = busRepository.findById(Long.valueOf(busId)).orElseThrow(() -> new ResourceNotFoundException("Bus not found with Id: " + busId));
        bus = BusMapper.MapRouteToBus(createRouteDTO, bus);
        try {
            busRepository.save(bus);
        } catch (Exception e) {
            throw new RuntimeException("Error saving bus");
        }
    }

    @Override
    public Page<Bus> fetchBuses(BusFetchRequestDTO requestDTO) {
        // Define th pageable variable
        Pageable pageable = PageRequest.of(requestDTO.getPage(), requestDTO.getSize());

        // Extract other parameters
        String queryString = requestDTO.getQuery();
        BusEnum.OperationalStatus operationalStatus = requestDTO.getOperationalStatus();

        // Build query
        try {
            return busRepository.findAll((Root<Bus> root, CriteriaQuery<?> query, CriteriaBuilder cb) -> {
                //
                List<Predicate> predicates = new ArrayList<>();

                // Filter based on Query status
                if (operationalStatus != null) {
                    predicates.add(cb.equal(root.get("operationalStatus"), operationalStatus));
                }

                // Filter title and description based on search input
                if (queryString != null && !queryString.isEmpty()) {
                    String searchText = "%" + queryString.toLowerCase().trim() + "%";
                    Predicate routePredicate = cb.like(cb.lower(root.get("routeName")), searchText);
                    Predicate busNumberPredicate = cb.like(cb.lower(root.get("busNumber")), searchText);
                    Predicate busModelPredicate = cb.like(cb.lower(root.get("busModel")), searchText);
                    predicates.add(cb.or(routePredicate, busNumberPredicate, busModelPredicate));
                }
                return cb.and(predicates.toArray(new Predicate[0]));
            }, pageable);
        } catch (Exception e) {
            log.error(e.getMessage());
            throw new RuntimeException("Unable to fetch buses");
        }
    }

    @Override
    public void deleteBus(String busId) {
        try {
            busRepository.deleteById(Long.valueOf(busId));
        } catch (Exception e) {
            log.error(e.getMessage());
            throw new RuntimeException("Unable to delete bus " + e.getMessage());
        }
    }

    @Override
    public void updateBusDetails(CreateBusDTO createBusDTO, String busId) {
        // Fetch bus
        Bus bus = busRepository.findById(Long.valueOf(busId)).orElseThrow(() -> new ResourceNotFoundException("Bus not found with Id: " + busId));

        // Update and save bus details
        bus = BusMapper.MapToBus(createBusDTO, bus);
        try {
            busRepository.save(bus);
        } catch (Exception e) {
            log.error(e.getMessage());
            throw new RuntimeException("Error updating bus " + e.getMessage());
        }
    }

    // I just added this code implementation
    @Override
    public Optional<Bus> fetchById(Long id) {
        // TODO Auto-generated method stub
        Optional<Bus> optionalBus = busRepository.findById(id);
        return optionalBus;
    }
}
