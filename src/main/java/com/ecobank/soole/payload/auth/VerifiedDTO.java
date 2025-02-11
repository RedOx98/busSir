package com.ecobank.soole.payload.auth;

import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.media.Schema.RequiredMode;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class VerifiedDTO {

    @NotBlank(message = "can not be blank")
    @Schema(description = "Verified", example = "false", requiredMode = RequiredMode.REQUIRED, defaultValue = "false")
    private String verified;
}
