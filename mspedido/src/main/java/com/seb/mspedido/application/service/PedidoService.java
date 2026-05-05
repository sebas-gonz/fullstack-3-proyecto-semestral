package com.seb.mspedido.application.service;

import com.seb.mspedido.application.mapper.DetalleDomainMapper;
import com.seb.mspedido.application.mapper.PedidoDomainMapper;
import com.seb.mspedido.application.port.in.PedidoInputPort;
import com.seb.mspedido.application.port.in.command.pedido.PedidoInputCommand;
import com.seb.mspedido.application.port.out.PedidoOutputPort;
import com.seb.mspedido.domain.model.Detalle;
import com.seb.mspedido.domain.model.Pedido;
import com.seb.mspedido.domain.model.Ubicacion;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@AllArgsConstructor
@Slf4j
public class PedidoService implements PedidoInputPort {
    private final PedidoOutputPort  pedidoOutputPort;
    private final DetalleDomainMapper  detalleDomainMapper;
    private final PedidoDomainMapper pedidoDomainMapper;
    @Override
    public Pedido guardarPedido(PedidoInputCommand pedidoInputCommand) {
        Pedido pedido = pedidoDomainMapper.toDomain(pedidoInputCommand);
        return pedidoOutputPort.guardarPedido(pedido);
    }

    @Override
    public Pedido guardarPedido(Pedido pedido) {
        return pedidoOutputPort.guardarPedido(pedido);
    }

    @Override
    public Pedido obtenerPedido(UUID pedidoId) {
        return pedidoOutputPort.obtenerPedido(pedidoId).orElseThrow(
                () -> new RuntimeException("Pedido no encontrado: " + pedidoId)
        );
    }

    @Override
    public Pedido actualizarPedido(UUID pedidoId, PedidoInputCommand pedidoInputCommand) {
        Pedido pedido = pedidoOutputPort.obtenerPedido(pedidoId).orElseThrow(
                () -> new RuntimeException("Pedido no encontrado: " + pedidoId)
        );
        Ubicacion ubicacionDestino = pedidoDomainMapper.toDomain(pedidoInputCommand.ubicacionDestino());
        List<Detalle> detalles = detalleDomainMapper.toDomainList(pedidoInputCommand.detalles());
        //Ubicacion del pedido actualizado
        pedido.setUbicacionDestino(ubicacionDestino);
        pedido.setDetalles(detalles);
        //Detalles del pedido actualizado
        pedido.setUsuarioId(pedidoInputCommand.usuarioId());
        pedido.calcularTotal();
        return pedidoOutputPort.guardarPedido(pedido);
    }

    @Override
    public List<Pedido> obtenerPedidos() {
        return pedidoOutputPort.obtenerPedidos();
    }

    @Override
    public void eliminarPedido(UUID pedidoId) {
        Pedido pedido = pedidoOutputPort.obtenerPedido(pedidoId).orElseThrow(
                () -> new RuntimeException("Pedido no encontrado: " + pedidoId)
        );
        pedidoOutputPort.eliminarPedido(pedidoId);
    }
}
