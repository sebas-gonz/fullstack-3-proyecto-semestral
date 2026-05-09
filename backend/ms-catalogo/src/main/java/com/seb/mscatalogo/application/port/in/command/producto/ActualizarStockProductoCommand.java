package com.seb.mscatalogo.application.port.in.command.producto;

import java.util.UUID;

public record ActualizarStockProductoCommand(UUID productoId, Integer cantidadTotal) {
}
