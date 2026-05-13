package com.seb.mspedido.infrastructure.adapter.in.kafka.dto.stock;

import java.util.UUID;

public record StockDescontadoEventDto(UUID pedidoId,
                                      boolean exito) {
}
