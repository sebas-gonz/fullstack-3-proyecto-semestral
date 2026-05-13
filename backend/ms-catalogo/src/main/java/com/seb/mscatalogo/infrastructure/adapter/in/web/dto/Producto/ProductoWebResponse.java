package com.seb.mscatalogo.infrastructure.adapter.in.web.dto.Producto;

import com.fasterxml.jackson.annotation.JsonFormat;

import java.math.BigDecimal;
import java.time.Instant;
import java.time.LocalDateTime;
import java.util.UUID;

public record ProductoWebResponse(UUID productoId,
                                  String sku,
                                  String nombre,
                                  String descripcion,
                                  BigDecimal precioBase,
                                  Integer cantidadTotal,
                                  @JsonFormat(shape = JsonFormat.Shape.STRING)
                                  Instant fechaRegistro
                                  ) {
}
