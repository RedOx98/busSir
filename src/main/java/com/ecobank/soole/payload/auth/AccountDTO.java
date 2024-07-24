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
public class AccountDTO {

    @Email
    @Schema(description = "Email address", example = "example@gmail.com", requiredMode = RequiredMode.REQUIRED)
    private String email;

    @Size(min = 6, max = 20)
    @Schema(description = "Password", example = "password", 
    requiredMode = RequiredMode.REQUIRED, minLength = 6, maxLength = 20)
    private String password_hash;

    @NotBlank
    private String level;

    @NotBlank
    private String telephone;

    @NotBlank
    private String firstName;

    @NotBlank
    private String lastName;

    @NotBlank
    private String username;

    @NotBlank
    private String verified;

    @NotBlank
    private String route;

    @NotBlank
    private String department;

    @NotBlank
    private String affiliate;

    @NotBlank
    @Size(min = 5, max = 7)
    @Schema(description = "Staff ID", example = "10928", 
    requiredMode = RequiredMode.REQUIRED, minLength = 5, maxLength = 7)
    private String staff_id;
}
