package com.seb.mspedido.application.service;

import com.seb.mspedido.application.mapper.DetalleDomainMapper;
import com.seb.mspedido.application.mapper.PedidoDomainMapper;
import com.seb.mspedido.application.port.in.PedidoInputPort;
import com.seb.mspedido.application.port.in.command.pedido.PedidoInputCommand;
import com.seb.mspedido.application.port.in.command.stock.StockDescontadoCommand;
import com.seb.mspedido.application.port.out.InventarioOutputPort;
import com.seb.mspedido.application.port.out.PedidoEventPubilsherPort;
import com.seb.mspedido.application.port.out.PedidoOutputPort;
import com.seb.mspedido.application.port.out.ProductoCatalogoOutputPort;
import com.seb.mspedido.domain.model.*;
import com.seb.mspedido.infrastructure.adapter.out.rest.InventarioRestAdapter;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.support.TransactionSynchronization;
import org.springframework.transaction.support.TransactionSynchronizationManager;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.List;
import java.util.UUID;

@Service
@AllArgsConstructor
@Slf4j
public class PedidoService implements PedidoInputPort {
    private final PedidoOutputPort  pedidoOutputPort;
    private final ProductoCatalogoOutputPort productoCatalogoOutputPort;
    private final DetalleDomainMapper  detalleDomainMapper;
    private final PedidoDomainMapper pedidoDomainMapper;
    private final PedidoEventPubilsherPort  pedidoEventPubilsherPort;
    private final InventarioOutputPort   inventarioOutputPort;
    @Override
    @Transactional
    public Pedido guardarPedido(PedidoInputCommand pedidoInputCommand) {
        Pedido pedido = pedidoDomainMapper.toDomain(pedidoInputCommand);
        if (pedido.getDetalles() == null || pedido.getDetalles().isEmpty()) {
            throw new IllegalArgumentException("El pedido debe contener al menos un detalle");
        }
        pedido.getDetalles().forEach(detalle -> {
            if (detalle.getDetalleId() == null) {
                detalle.setDetalleId(UUID.randomUUID());
            }
            ProductoReferencia producto = productoCatalogoOutputPort.getProductoReferencia(detalle.getProductoId())
                    .orElseThrow(
                            () -> new RuntimeException("No existe el producto referencia " + detalle.getProductoId())
                    );
            detalle.setPrecio(producto.precio());
            detalle.setFechaRegistro(Instant.now());
        });
        Ubicacion ubi = inventarioOutputPort.obtenerUbicacionInventario(pedido.getDetalles().get(0).getInventarioId());
        pedido.setOrigen(ubi);
        pedido.calcularTotal();
        pedido.setEstado(Estado.PENDIENTE);
        Pedido persistido = pedidoOutputPort.guardarPedido(pedido);
        TransactionSynchronizationManager.registerSynchronization(new TransactionSynchronization() {
            @Override
            public void afterCommit() {
                pedidoEventPubilsherPort.publicarPedidoCreado(persistido);
            }
        });
        return persistido;
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
    @Transactional
    public Pedido actualizarPedido(UUID pedidoId, PedidoInputCommand pedidoInputCommand) {
        Pedido pedido = pedidoOutputPort.obtenerPedido(pedidoId).orElseThrow(
                () -> new RuntimeException("Pedido no encontrado: " + pedidoId)
        );
        Ubicacion ubicacionDestino = pedidoDomainMapper.toDomain(pedidoInputCommand.destino());
        List<Detalle> detalles = detalleDomainMapper.toDomainList(pedidoInputCommand.detalles());
        if (!detalles.isEmpty()) {
            Ubicacion origen = inventarioOutputPort.obtenerUbicacionInventario(pedidoInputCommand.detalles().get(0).inventarioId());
            pedido.setOrigen(origen);
        }
        pedido.setDestino(ubicacionDestino);
        pedido.setDetalles(detalles);
        pedido.setUsuarioId(pedidoInputCommand.usuarioId());
        pedido.calcularTotal();
        Pedido persistido = pedidoOutputPort.guardarPedido(pedido);
        TransactionSynchronizationManager.registerSynchronization(new TransactionSynchronization() {
            @Override
            public void afterCommit() {
                pedidoEventPubilsherPort.publicarPedidoCreado(persistido);
            }
        });
        return persistido;
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

    @Override
    @Transactional
    public void confirmarStockYPrepararPedido(StockDescontadoCommand command) {
        Pedido pedido = pedidoOutputPort.obtenerPedido(command.pedidoId()).orElseThrow(
                () -> new RuntimeException("Pedido no encontrado: " + command.pedidoId())
        );
        pedido.setEstado(Estado.PREPARANDO);
        pedidoEventPubilsherPort.publicarPedidoPreparado(pedidoOutputPort.guardarPedido(pedido));
    }

    @Override
    @Transactional
    public void marcarPedidoEnviado(UUID pedidoId) {
        Pedido pedido = pedidoOutputPort.obtenerPedido(pedidoId).orElseThrow(
                () -> new RuntimeException("Pedido no encontrado: " + pedidoId)
        );
        pedido.setEstado(Estado.ENVIADO);
        pedidoOutputPort.guardarPedido(pedido);
    }

    @Override
    public void marcarPedidoEntregado(UUID pedidoId) {
        Pedido pedido = pedidoOutputPort.obtenerPedido(pedidoId).orElseThrow(
                () -> new RuntimeException("Pedido no encontrado: " + pedidoId)
        );
        pedido.setEstado(Estado.ENTREGADO);
        pedido.setFechaEntrega(Instant.now());
        pedidoOutputPort.guardarPedido(pedido);
    }
}
