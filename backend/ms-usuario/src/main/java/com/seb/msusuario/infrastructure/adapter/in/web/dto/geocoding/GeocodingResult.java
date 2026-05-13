package com.seb.msusuario.infrastructure.adapter.in.web.dto.geocoding;

import java.io.Serializable;

public record GeocodingResult(GeocodingLocation location) implements Serializable {}
