package com.seb.msinventario.application.port.in.command.stock;

import com.seb.msinventario.application.port.in.command.producto.ProductoPedidoCommand;

import java.util.List;
import java.util.UUID;

public record DescontarStockCommand(UUID pedidoId,
                                    List<ProductoPedidoCommand> productosPedidos
                                    ) {
}
