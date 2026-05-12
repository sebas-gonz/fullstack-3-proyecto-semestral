package com.seb.msinventario.application.port.in.command.stock;

import java.util.UUID;

public record ProductoStockCommand(UUID productoId,Integer cantidadTotal) {
}
