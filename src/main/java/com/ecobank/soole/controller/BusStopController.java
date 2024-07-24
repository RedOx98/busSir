package com.ecobank.soole.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.ecobank.soole.payload.ResponseDTO;
import com.ecobank.soole.payload.bus.CreateBusStopDTO;
import com.ecobank.soole.services.BusStopService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@Validated
@RequestMapping("/api/v1/bstp")
@RequiredArgsConstructor
@Tag(name = "Bus stop Controller", description = "Controller for Bus stop management")
public class BusStopController {
    private final BusStopService busStopService;

    @DeleteMapping("/delete")
    @ApiResponse(responseCode = "200", description = "Delete bus")
    @ApiResponse(responseCode = "401", description = "Token missing")
    @ApiResponse(responseCode = "403", description = "Token error")
    @Operation(summary = "Admin Delete bus-stop")
    @SecurityRequirement(name = "soole-demo-api")
    public ResponseEntity<ResponseDTO> deleteBusStop(@RequestParam String busStopId) {
        busStopService.deleteBusStop(busStopId);
        return ResponseEntity.status(HttpStatus.OK).body(new ResponseDTO(HttpStatus.OK, "Bus stop deleted successfully"));
    }

    @PostMapping(value = "/add", produces = "application/json")
    @ResponseStatus(HttpStatus.CREATED)
    @ApiResponse(responseCode = "201", description = "Bus added")
    @ApiResponse(responseCode = "200", description = "success")
    @Operation(summary = "Admin Create bus-stop")
    @SecurityRequirement(name = "soole-demo-api")
    public ResponseEntity<ResponseDTO> createBusStop(@Valid @RequestBody CreateBusStopDTO createBusStopDTO, @RequestParam String busId) {
        busStopService.createBusStop(createBusStopDTO, busId);
        return ResponseEntity.status(HttpStatus.CREATED).body(new ResponseDTO(HttpStatus.CREATED, "Bus stop added successfully!"));
    }

    @PutMapping(value = "/update", produces = "application/json", consumes = "application/json")
    @ApiResponse(responseCode = "200", description = "List of users")
    @ApiResponse(responseCode = "401", description = "Token missing")
    @ApiResponse(responseCode = "403", description = "Token error")
    @Operation(summary = "Admin Update bus-stop")
    @SecurityRequirement(name = "soole-demo-api")
    public ResponseEntity<ResponseDTO> updateBusStop(@Valid @RequestBody CreateBusStopDTO createBusStopDTO, @RequestParam String busStopId) {
        busStopService.updateBusStop(createBusStopDTO, busStopId);
        return ResponseEntity.status(HttpStatus.OK).body(new ResponseDTO(HttpStatus.OK, "Bus stop updated successfully"));
    }
}
