package com.ecobank.soole.payload.bus;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class CreateBusStopDTO {
    private String busStopName;
}