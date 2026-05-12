package com.seb.mspedido.application.port.in;

import com.seb.mspedido.application.port.in.command.pedido.PedidoInputCommand;
import com.seb.mspedido.application.port.in.command.stock.StockDescontadoCommand;
import com.seb.mspedido.domain.model.Pedido;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;


import java.util.List;
import java.util.UUID;

public interface PedidoInputPort {
    Pedido guardarPedido(PedidoInputCommand  pedidoInputCommand);
    Pedido guardarPedido(Pedido pedido);
    Pedido obtenerPedido(UUID pedidoId);
    Pedido actualizarPedido(UUID pedidoId,PedidoInputCommand pedidoInputCommand);
    Page<Pedido> obtenerPedidos(Pageable pageable);
    void eliminarPedido(UUID pedidoId);
    void confirmarStockYPrepararPedido(StockDescontadoCommand command);
    void marcarPedidoEnviado(UUID pedidoId);

    void marcarPedidoEntregado(UUID pedidoId);
    List<Pedido> obtenerPedidosPorUsuario(UUID idUsuario);
}
