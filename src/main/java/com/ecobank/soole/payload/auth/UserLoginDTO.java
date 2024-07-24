package com.ecobank.soole.payload.auth;

import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.media.Schema.RequiredMode;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class UserLoginDTO {
    
    @Email
    @Schema(description = "Email address", example = "example@gmail.com", requiredMode = RequiredMode.REQUIRED)
    private String email;

    @Size(min = 8, max = 20)
    @Schema(description = "Password", example = "password", 
    requiredMode = RequiredMode.REQUIRED, minLength = 8, maxLength = 20)
    private String password;
}
