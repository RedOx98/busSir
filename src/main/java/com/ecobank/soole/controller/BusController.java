package com.ecobank.soole.controller;

import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.ecobank.soole.models.Bus;
import com.ecobank.soole.payload.PagedResponseDTO;
import com.ecobank.soole.payload.ResponseDTO;
import com.ecobank.soole.payload.bus.BusFetchRequestDTO;
import com.ecobank.soole.payload.bus.CreateBusDTO;
import com.ecobank.soole.payload.bus.CreateRouteDTO;
import com.ecobank.soole.services.BusService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequiredArgsConstructor
@RestController
@Validated
@RequestMapping("/api/v1/bus")
@Tag(name = "Bus Controller", description = "Controller for Bus management")
public class BusController {
    // Bus service
    private final BusService busService;

    @PostMapping(value = "/add", produces = "application/json")
    @ResponseStatus(HttpStatus.CREATED)
    @ApiResponse(responseCode = "201", description = "Bus added")
    @ApiResponse(responseCode = "200", description = "success")
    @Operation(summary = "Admin Create bus")
    @SecurityRequirement(name = "soole-demo-api")
    public ResponseEntity<ResponseDTO> createBus(@Valid @RequestBody CreateBusDTO createBusDTO) {
        busService.createBus(createBusDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(new ResponseDTO(HttpStatus.CREATED, "Bus created successfully"));
    }

    @GetMapping("/list")
    @ApiResponse(responseCode = "200", description = "List of buses")
    @ApiResponse(responseCode = "401", description = "Token missing")
    @ApiResponse(responseCode = "403", description = "Token error")
    @Operation(summary = "List Buses API")
    @SecurityRequirement(name = "soole-demo-api")
    public ResponseEntity<PagedResponseDTO<Bus>> fetchBuses(@ModelAttribute BusFetchRequestDTO busFetchRequestDTO) {
        Page<Bus> buses = busService.fetchBuses(busFetchRequestDTO);
        return ResponseEntity.status(HttpStatus.OK).body(new PagedResponseDTO<>(buses));
    }

    @DeleteMapping("/delete")
    @ApiResponse(responseCode = "200", description = "Delete bus")
    @ApiResponse(responseCode = "401", description = "Token missing")
    @ApiResponse(responseCode = "403", description = "Token error")
    @Operation(summary = "Admin Delete bus")
    @SecurityRequirement(name = "soole-demo-api")
    public ResponseEntity<ResponseDTO> deleteBus(@Valid @RequestParam String busId) {
        busService.deleteBus(busId);
        return ResponseEntity.status(HttpStatus.OK).body(new ResponseDTO(HttpStatus.OK, "Bus deleted successfully"));
    }

    @PutMapping(value = "/update", produces = "application/json", consumes = "application/json")
    @ApiResponse(responseCode = "200", description = "Updated bus")
    @ApiResponse(responseCode = "401", description = "Token missing")
    @ApiResponse(responseCode = "403", description = "Token error")
    @ApiResponse(responseCode = "400", description = "Invalid bus")
    @Operation(summary = "Admin Update bus")
    @SecurityRequirement(name = "soole-demo-api")
    public ResponseEntity<ResponseDTO> updateBusDetails(@RequestParam String busId, @Valid @RequestBody CreateBusDTO createBusDTO) {
        busService.updateBusDetails(createBusDTO, busId);
        return ResponseEntity.status(HttpStatus.OK).body(new ResponseDTO(HttpStatus.OK, "Bus details updated successfully"));
    }

    @PostMapping(value = "/route/add", produces = "application/json", consumes = "application/json")
    @ResponseStatus(HttpStatus.OK)
    @ApiResponse(responseCode = "417", description = "Invalid token!")
    @ApiResponse(responseCode = "201", description = "route changed")
    @ApiResponse(responseCode = "200", description = "success")
    @Operation(summary = "Admin to create route")
    @SecurityRequirement(name = "soole-demo-api")
    public ResponseEntity<ResponseDTO> addRouteDetails(@Valid @RequestBody CreateRouteDTO createRouteDTO, @RequestParam String busId) {
        busService.addRouteDetails(createRouteDTO, busId);
        return ResponseEntity.status(HttpStatus.CREATED).body(new ResponseDTO(HttpStatus.CREATED, "Bus route added successfully"));
    }

    @PutMapping(value = "/route/update", produces = "application/json", consumes = "application/json")
    @ApiResponse(responseCode = "200", description = "List of users")
    @ApiResponse(responseCode = "401", description = "Token missing")
    @ApiResponse(responseCode = "403", description = "Token error")
    @Operation(summary = "Admin Update route")
    @SecurityRequirement(name = "soole-demo-api")
    public ResponseEntity<ResponseDTO> updateRouteDetails(@Valid @RequestBody CreateRouteDTO createRouteDTO, @RequestParam String busId) {
        busService.addRouteDetails(createRouteDTO, busId);
        return ResponseEntity.status(HttpStatus.CREATED).body(new ResponseDTO(HttpStatus.CREATED, "Bus route added successfully"));
    }
}