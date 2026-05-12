package com.seb.mspedido.infrastructure.adapter.in.kafka.dto.producto;

import java.math.BigDecimal;
import java.util.UUID;

public record ProductoPrecioEventDto(UUID productoId,String nombre, BigDecimal precio) {

}
