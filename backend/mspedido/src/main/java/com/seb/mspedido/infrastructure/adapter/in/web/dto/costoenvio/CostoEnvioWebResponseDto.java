package com.seb.mspedido.infrastructure.adapter.in.web.dto.costoenvio;

import com.seb.mspedido.infrastructure.adapter.in.web.dto.ubicacion.UbicacionWebRequest;

import java.math.BigDecimal;

public record CostoEnvioWebResponseDto(BigDecimal costoEnvio, Double distanciaKm) {
}
