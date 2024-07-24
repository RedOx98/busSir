package com.ecobank.soole.payload.bus;

import com.ecobank.soole.util.constants.BusEnum;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class CreateBusDTO {
    @NotBlank(message = "Bus number must not be blank")
    @Size(min = 3, message = "Bus number must be at least 3 characters long")
    private String busNumber;

    private BusEnum.OperationalStatus operationalStatus = BusEnum.OperationalStatus.ACTIVE;

    @NotBlank(message = "Bus model must not be blank")
    @Size(min = 3, message = "Bus model must be at least 3 characters long")
    private String busModel;

    @Min(value = 1)
    private int busCapacity;

    @NotBlank(message = "Bus color must not be blank")
    private String busColor;

//    @NotBlank(message = "Route name must not be blank")
//    @Size(min = 3, message = "Route name must be at least 3 characters long")
//    private String routeName;
}
