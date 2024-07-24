package com.ecobank.soole.payload.auth;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class TokenViewDTO {
    private String token;

    private String authorities;

    private String level;

    private String firstName;

    private String lastName;
    
    private String username;
}
