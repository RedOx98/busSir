package com.ecobank.soole.util.constants;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.http.HttpStatus;

import java.time.LocalDateTime;

@Schema(name = "Error Response", description = "Schema to hold error response information")
@Data
@AllArgsConstructor
public class ErrorResponseDTO {
    @Schema(description = "API path invoked by client")
    private String apiPath;

    @Schema(description = "Error code")
    private HttpStatus errorCode;

    @Schema(description = "Error message details")
    private Object errorMessage;

    @Schema(description = "Time representing when the error happened")
    private LocalDateTime errorTime;
}
