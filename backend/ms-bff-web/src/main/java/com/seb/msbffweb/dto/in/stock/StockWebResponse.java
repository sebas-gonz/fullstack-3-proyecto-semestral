package com.seb.msbffweb.dto.in.stock;

import java.time.Instant;
import java.util.UUID;

public record StockWebResponse(
        UUID stockId,
        UUID productoId,
        String lote,
        Integer cantidad,
        Instant fechaRegistroLote,
        Instant fechaRegistro
) {
}
