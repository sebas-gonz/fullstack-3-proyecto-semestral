package com.seb.mspedido.application.service;

import com.seb.mspedido.application.mapper.DetalleDomainMapper;
import com.seb.mspedido.application.mapper.PedidoDomainMapper;
import com.seb.mspedido.application.port.in.PedidoInputPort;
import com.seb.mspedido.application.port.in.command.pedido.PedidoInputCommand;
import com.seb.mspedido.application.port.in.command.stock.StockDescontadoCommand;
import com.seb.mspedido.application.port.in.query.CotizacionEnvioQuery;
import com.seb.mspedido.application.port.out.*;
import com.seb.mspedido.domain.model.*;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.support.TransactionSynchronization;
import org.springframework.transaction.support.TransactionSynchronizationManager;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.Arrays;
import java.util.List;
import java.util.Set;
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
    private final DistanciaOutputPort  distanciaOutputPort;
    @Override
    @Transactional
    public Pedido guardarPedido(PedidoInputCommand pedidoInputCommand) {
        Pedido pedido = pedidoDomainMapper.toDomain(pedidoInputCommand);
        if (pedido.getDetalles() == null || pedido.getDetalles().isEmpty()) {
            throw new IllegalArgumentException("El pedido debe contener al menos un detalle");
        }
        //Obtenemos la informacion del producto consultando la tabla cache del producto.
        pedido.getDetalles().forEach(detalle -> {
            ProductoCache producto = productoCatalogoOutputPort.getProductoReferencia(detalle.getProductoId())
                    .orElseThrow(
                            () -> new RuntimeException("No existe el producto referencia " + detalle.getProductoId())
                    );
            detalle.setPrecio(producto.precio());
            detalle.setFechaRegistro(Instant.now());
        });
        //Ubicacion del origen
        Ubicacion ubi = inventarioOutputPort.obtenerUbicacionInventario(pedido.getDetalles().getFirst().getInventarioId());
        pedido.setOrigen(ubi);
        //Instancia de el objeto para realizar la consulta del costo de envio
        CotizacionEnvioQuery cotizacionEnvioQuery = new CotizacionEnvioQuery(pedido.getOrigen().getLatitude(),pedido.getOrigen().getLongitude(),
                pedido.getDestino().getLatitude(),pedido.getDestino().getLongitude());
        CotizacionEnvioResultado cotizacionCostoEnvio = this.cotizarCostoDeEnvio(cotizacionEnvioQuery);
        pedido.setCostoEnvio(cotizacionCostoEnvio.costoEnvio());
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
    public Page<Pedido> obtenerPedidos(Pageable pageable) {
        return pedidoOutputPort.obtenerPedidos(pageable);
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
    @Transactional
    public void marcarPedidoEntregado(UUID pedidoId) {
        Pedido pedido = pedidoOutputPort.obtenerPedido(pedidoId).orElseThrow(
                () -> new RuntimeException("Pedido no encontrado: " + pedidoId)
        );
        pedido.setEstado(Estado.ENTREGADO);
        pedido.setFechaEntrega(Instant.now());
        pedidoOutputPort.guardarPedido(pedido);
    }

    @Override
    public Page<Pedido> obtenerPedidosPorUsuario(UUID idUsuario,  Pageable pageable) {
        return pedidoOutputPort.obtenerPedidosPorUsuario(idUsuario, pageable);
    }

    @Override
    public CotizacionEnvioResultado cotizarCostoDeEnvio(CotizacionEnvioQuery cotizacionEnvioQuery) {
        double distanciaKm = distanciaOutputPort.obtenerDistancia(cotizacionEnvioQuery.destinoLatitude(),cotizacionEnvioQuery.destinoLongitude(),
                cotizacionEnvioQuery.origenLatitude(),cotizacionEnvioQuery.origenLongitude());
        long costoEnvio = Math.round(distanciaKm * 2000);
        if (costoEnvio < 2000) costoEnvio = 2000;
        return new CotizacionEnvioResultado(BigDecimal.valueOf(costoEnvio),distanciaKm);
    }

    @Override
    public Page<Pedido> buscarPedido(String param, Pageable  pageable) {
        if (param == null || param.isBlank()){
            return pedidoOutputPort.obtenerPedidos(pageable);
        }
        return pedidoOutputPort.buscarPedido(param, pageable);
    }
    @Override
    public List<String> obtenerEstadosPedidos(){
        List<String> estadosEnvios = Arrays.stream(Estado.values()).map(Enum::name).toList();
        return estadosEnvios;
    }

    @Override
    public Page<Pedido> obtenerPedidosPorEstado(String estadoPedido, Pageable pageable) {
        try{
            if (estadoPedido == null || estadoPedido.isBlank()){
                return pedidoOutputPort.obtenerPedidos(pageable);
            }
            Estado estado = Enum.valueOf(Estado.class, estadoPedido.toUpperCase());
            return pedidoOutputPort.obtenerPedidosPorEstado(estado, pageable);
        } catch (IllegalArgumentException e){
            return pedidoOutputPort.obtenerPedidos(pageable);
        }
    }

    @Override
    public List<Pedido> obtenerPedidosPorIds(Set<UUID> pedidoIds) {
        if (pedidoIds == null || pedidoIds.isEmpty()){
            return List.of();
        }
        return pedidoOutputPort.obtenerPedidosPorIds(pedidoIds);
    }

}
