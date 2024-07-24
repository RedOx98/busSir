package com.ecobank.soole.payload.bus;

import com.ecobank.soole.util.constants.BusEnum;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class BusFetchRequestDTO {
    private int page = 0;  // default page number
    private int size = 10; // default page size
    private String query = "";
    private String sortDirection = "DESC";
    private BusEnum.OperationalStatus operationalStatus;
}
