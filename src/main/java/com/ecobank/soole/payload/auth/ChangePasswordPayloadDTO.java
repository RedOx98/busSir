package com.ecobank.soole.payload.auth;

import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.media.Schema.RequiredMode;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ChangePasswordPayloadDTO {
    @Size(min = 8, max = 20)
    @Schema(description = "Password", example = "password", 
    requiredMode = RequiredMode.REQUIRED, minLength = 8, maxLength = 20)
    private String password;

    // @NotBlank
    // @Schema(description = "Token", example = "token", 
    // requiredMode = RequiredMode.REQUIRED, minLength = 8, maxLength = 20)
    // private String token;
}
