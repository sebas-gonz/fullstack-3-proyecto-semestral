package com.seb.mspedido.application.port.in.command.stock;

import java.util.UUID;

public record StockDescontadoCommand(UUID pedidoId,
                                     boolean exito) {
}
