package com.seb.msinventario.infrastructure.adapter.in.web.dto.stock;

import com.fasterxml.jackson.annotation.JsonFormat;

import java.time.Instant;
import java.util.UUID;

public record StockWebResponse(UUID stockId,
                               UUID productoId,
                               String lote,
                               Integer cantidad,
                               @JsonFormat(shape = JsonFormat.Shape.STRING)
                               Instant fechaRegistroLote,
                               @JsonFormat(shape = JsonFormat.Shape.STRING)
                               Instant fechaRegistro) {
}
