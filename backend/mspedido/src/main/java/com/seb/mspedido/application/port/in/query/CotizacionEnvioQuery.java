package com.seb.mspedido.application.port.in.query;

import java.math.BigDecimal;

public record CotizacionEnvioQuery(
        BigDecimal origenLatitude,
        BigDecimal origenLongitude,
        BigDecimal destinoLatitude,
        BigDecimal destinoLongitude
) {
}
