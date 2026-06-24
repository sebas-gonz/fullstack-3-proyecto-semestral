package com.seb.msbffweb.dto.in.pedido;

import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;

public record CotizacionEnvioRequestDto(
        @NotNull(message = "La latitud de origen es obligatoria")
        BigDecimal origenLatitude,
        @NotNull(message = "La longitud de origen es obligatoria")
        BigDecimal origenLongitude,
        @NotNull(message = "La latitud de destino es obligatoria")
        BigDecimal destinoLatitude,
        @NotNull(message = "La longitud de destino es obligatoria")
        BigDecimal destinoLongitude
) {
}
