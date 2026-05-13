package com.seb.mscatalogo.infrastructure.adapter.in.kafka.dto;

import java.util.UUID;

public record ProductoStockEventDto(UUID productoId, Integer cantidadTotal) {
}
