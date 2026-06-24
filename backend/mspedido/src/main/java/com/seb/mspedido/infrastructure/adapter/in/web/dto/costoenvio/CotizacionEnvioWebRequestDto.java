package com.seb.mspedido.infrastructure.adapter.in.web.dto.costoenvio;

import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;

public record CotizacionEnvioWebRequestDto(
        BigDecimal origenLatitude,
        BigDecimal origenLongitude,
        BigDecimal destinoLatitude,
        BigDecimal destinoLongitude
) {
}
