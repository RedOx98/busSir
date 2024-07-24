package com.ecobank.soole.payload.booking;

import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class BookingViewDTO {
    

    private LocalDateTime time_of_departure;

    private LocalDateTime createdAt;

    private String take_off_point;

    private String drop_off_point;

    private String status;

    private String route;
    private String board;
}
