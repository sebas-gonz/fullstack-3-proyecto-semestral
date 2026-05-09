package com.seb.mspedido.infrastructure.adapter.in.web.dto.detalle;

import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;
import java.util.UUID;

public record DetalleWebRequest(@NotNull(message = "Id del producto es nulo")
                                 UUID productoId,
                                 @NotNull(message = "Id de la bodega es nulo")
                                 UUID inventarioId,
                                 @NotNull(message = "La cantidad de nula")
                                 Integer cantidad
                                 ) {
}
