package com.seb.mspedido.infrastructure.adapter.in.web.dto.detalle;

import com.fasterxml.jackson.annotation.JsonFormat;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.UUID;

public record DetalleWebResponse(UUID detalleId,
                                 UUID productoId,
                                 UUID inventarioId,
                                 BigDecimal precio,
                                 Integer cantidad,
                                 @JsonFormat(shape = JsonFormat.Shape.STRING)
                                 Instant fechaRegistro) {
}
