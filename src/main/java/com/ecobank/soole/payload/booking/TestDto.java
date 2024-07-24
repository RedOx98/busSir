package com.ecobank.soole.payload.booking;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
// @NoArgsConstructor
public class TestDto {

    private Long userId;
    private Long busId;
    private String route;
    private String drop_off_point;
}
