package com.seb.msinventario.application.port.in.command.producto;

import java.util.UUID;

public record ProductoPedidoCommand(UUID inventarioId,
                                    UUID productoId,
                                    Integer cantidad) {
}
