package com.seb.msinventario.application.port.in.command.stock;

import java.time.Instant;
import java.util.UUID;

public record StockInputCommand(UUID productoId,
                                String lote,
                                Integer cantidad,
                                Instant fechaRegistroLote) {
}
