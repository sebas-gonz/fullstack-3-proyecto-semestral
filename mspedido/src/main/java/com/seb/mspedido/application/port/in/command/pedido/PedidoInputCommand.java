package com.seb.mspedido.application.port.in.command.pedido;

import com.seb.mspedido.application.port.in.command.detalle.DetalleInputCommand;

import java.util.List;
import java.util.UUID;

public record PedidoInputCommand(UUID usuarioId,
                                 UbicacionInputCommand destino,
                                 List<DetalleInputCommand> detalles) {
}
