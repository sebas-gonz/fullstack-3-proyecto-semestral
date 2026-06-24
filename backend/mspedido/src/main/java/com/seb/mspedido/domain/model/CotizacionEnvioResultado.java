package com.seb.mspedido.domain.model;

import java.math.BigDecimal;

public record CotizacionEnvioResultado(BigDecimal costoEnvio, Double distanciaKm) {
}
