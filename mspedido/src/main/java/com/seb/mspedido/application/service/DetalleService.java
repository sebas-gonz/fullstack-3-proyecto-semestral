package com.seb.mspedido.application.service;

import com.seb.mspedido.application.mapper.DetalleDomainMapper;
import com.seb.mspedido.application.mapper.PedidoDomainMapper;
import com.seb.mspedido.application.port.in.DetalleInputPort;
import com.seb.mspedido.application.port.in.command.detalle.DetalleInputCommand;
import com.seb.mspedido.application.port.out.PedidoOutputPort;
import com.seb.mspedido.domain.model.Detalle;
import com.seb.mspedido.domain.model.Pedido;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;
@Service
@Slf4j
@AllArgsConstructor
public class DetalleService implements DetalleInputPort {
    private final PedidoOutputPort  pedidoOutputPort;
    private final DetalleDomainMapper detalleDomainMapper;
    @Override
    public Detalle guardarDetalle(UUID pedidoId, DetalleInputCommand detalleInputCommand) {
        Pedido pedido = pedidoOutputPort.obtenerPedido(pedidoId).orElseThrow(
                () -> new RuntimeException("Pedido no encontrado")
        );
        Detalle detalle = detalleDomainMapper.toDomain(detalleInputCommand);
        pedido.getDetalles().add(detalle);
        return pedido.getDetalles().stream().filter(d -> d.getDetalleId().equals(detalle.getDetalleId()))
                .findFirst().orElseThrow(
                () -> new RuntimeException("Detalle no encontrado")
        );
    }

    @Override
    public Detalle guardarDetalle(UUID pedidoId, Detalle detalle) {
        Pedido pedido = pedidoOutputPort.obtenerPedido(pedidoId).orElseThrow(
                () -> new RuntimeException("Pedido no encontrado")
        );
        pedido.getDetalles().add(detalle);
        return pedido.getDetalles().stream().filter(d -> d.getDetalleId().equals(detalle.getDetalleId()))
                .findFirst().orElseThrow(
                        () -> new RuntimeException("Detalle no encontrado")
                );
    }

    @Override
    public Detalle obtenerDetalle(UUID pedidoId, UUID detalleId) {
        Pedido pedido = pedidoOutputPort.obtenerPedido(pedidoId)
                .orElseThrow(() -> new RuntimeException("Pedido no encontrado")
        );
        return pedido.getDetalles().stream().filter(d -> d.getDetalleId().equals(detalleId))
                .findFirst()
                .orElseThrow(() -> new RuntimeException("Detalle no encontrado"));
    }

    @Override
    public List<Detalle> obtenerDetallesPorPedido(UUID pedidoId) {
        return pedidoOutputPort.obtenerPedido(pedidoId)
                .orElseThrow(() -> new RuntimeException("Pedido no encontrado"))
                .getDetalles();
    }

    @Override
    public void eliminarDetalle(UUID pedidoId, UUID detalleId) {
        Pedido pedido = pedidoOutputPort.obtenerPedido(pedidoId)
                .orElseThrow( () -> new RuntimeException("Pedido no encontrado"));
        boolean existe = pedido.getDetalles().removeIf(d -> d.getDetalleId().equals(detalleId));
        if (!existe) {
            throw new RuntimeException("Detalle no encontrado");
        }
        pedidoOutputPort.guardarPedido(pedido);
    }
}
