package com.ecobank.soole.payload.auth;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ProfileDTO {

    private Long id;
    
    private String email;

    private String authorities;

    private String level;

    private String telephone;
}