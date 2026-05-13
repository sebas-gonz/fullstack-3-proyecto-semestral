package com.seb.mspedido.application.port.in.command.detalle;

import java.util.UUID;

public record DetalleInputCommand(UUID productoId,
                                  UUID inventarioId,
                                  Integer cantidad) {
}
