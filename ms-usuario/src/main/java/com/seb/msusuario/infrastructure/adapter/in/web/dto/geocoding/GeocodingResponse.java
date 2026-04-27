package com.seb.msusuario.infrastructure.adapter.in.web.dto.geocoding;

import java.util.List;

public record GeocodingResponse(List<GeocodingResult> results) {
}
