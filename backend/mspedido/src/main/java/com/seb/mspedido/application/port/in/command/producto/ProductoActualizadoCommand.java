package com.seb.mspedido.application.port.in.command.producto;

import java.math.BigDecimal;
import java.util.UUID;

public record ProductoActualizadoCommand(UUID productoId, String nombre, BigDecimal precio) {
}
