package com.ecobank.soole.payload.auth;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class StatsDTO {
    private int users;

    private int verified;

    private int pending;

    private int rejected;

    // private int buses;

    private int captainorbuses;
}
