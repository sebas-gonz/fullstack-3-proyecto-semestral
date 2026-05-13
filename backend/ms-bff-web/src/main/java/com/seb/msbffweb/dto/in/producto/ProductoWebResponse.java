package com.seb.msbffweb.dto.in.producto;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.UUID;

public record ProductoWebResponse(
        UUID productoId,
        String sku,
        String nombre,
        String descripcion,
        BigDecimal precioBase,
        Integer cantidadTotal,

        Instant fechaRegistro
) {
}
