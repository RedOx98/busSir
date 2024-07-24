package com.ecobank.soole.payload.booking;

import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.media.Schema.RequiredMode;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class BookingPayloadDTO {

    @NotNull
    @Schema(description = "userId", example = "1", requiredMode = RequiredMode.REQUIRED)
    private Long userId;

    @NotNull
    @Schema(description = "busId", example = "1", requiredMode = RequiredMode.REQUIRED)
    private Long busId;

    @NotBlank
    private String route;

    @NotBlank
    private String drop_off_point;
}
// public class BookingPayloadDTO {

//     @NotNull
//     @Schema(description = "userId", example = "1", requiredMode = RequiredMode.REQUIRED)
//     private Long userId;

//     @NotNull
//     @Schema(description = "busId", example = "1", requiredMode = RequiredMode.REQUIRED)
//     private Long busId;

//     @NotBlank
//     private String route;

//     @NotBlank
//     private String drop_off_point;
// }