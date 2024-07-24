package com.ecobank.soole.payload.auth;

import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.media.Schema.RequiredMode;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class UpdateAccountViewDTO {

    private Long id;

    private String email;

    private String level;

    private String telephone;

    private String firstName;

    private String lastName;

    private String username;

    private String verified;

    private String route;

    private String department;

    private String affiliate;

    private String status;

    private String staff_id;
}
