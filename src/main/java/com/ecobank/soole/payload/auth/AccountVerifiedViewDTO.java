package com.ecobank.soole.payload.auth;

import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class AccountVerifiedViewDTO {
    private Long id;

    private String email;

    private String authorities;

    private LocalDateTime createdAt;

    private String level;

    private String telephone;

    private String verfified;
}
