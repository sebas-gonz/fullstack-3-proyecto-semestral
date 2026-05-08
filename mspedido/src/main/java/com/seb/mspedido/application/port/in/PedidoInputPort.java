package com.seb.mspedido.application.port.in;

import com.seb.mspedido.application.port.in.command.pedido.PedidoInputCommand;
import com.seb.mspedido.application.port.in.command.stock.StockDescontadoCommand;
import com.seb.mspedido.domain.model.Pedido;

import java.util.List;
import java.util.UUID;

public interface PedidoInputPort {
    Pedido guardarPedido(PedidoInputCommand  pedidoInputCommand);
    Pedido guardarPedido(Pedido pedido);
    Pedido obtenerPedido(UUID pedidoId);
    Pedido actualizarPedido(UUID pedidoId,PedidoInputCommand pedidoInputCommand);
    List<Pedido> obtenerPedidos();
    void eliminarPedido(UUID pedidoId);
    void confirmarStockYPrepararPedido(StockDescontadoCommand command);
    void marcarPedidoEnviado(UUID pedidoId);

    void marcarPedidoEntregado(UUID pedidoId);
}
