package com.seb.msusuario.infrastructure.adapter.in.web.dto.geocoding;

import java.io.Serializable;
import java.math.BigDecimal;

public record GeocodingLocation(BigDecimal latitude, BigDecimal longitude) implements Serializable {
}
