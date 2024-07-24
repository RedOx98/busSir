package com.ecobank.soole.payload.auth;

import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.media.Schema.RequiredMode;
import jakarta.validation.constraints.Email;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class PasswordResetPayloadDTO {
    
    @Email
    @Schema(description = "Email address", example = "example@gmail.com", requiredMode = RequiredMode.REQUIRED)
    private String email;
}

