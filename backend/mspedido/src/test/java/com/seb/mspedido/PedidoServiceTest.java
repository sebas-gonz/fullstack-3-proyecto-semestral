package com.seb.mspedido;

import com.seb.mspedido.application.mapper.DetalleDomainMapper;
import com.seb.mspedido.application.mapper.PedidoDomainMapper;
import com.seb.mspedido.application.port.in.command.detalle.DetalleInputCommand;
import com.seb.mspedido.application.port.in.command.pedido.PedidoInputCommand;
import com.seb.mspedido.application.port.in.command.pedido.UbicacionInputCommand;
import com.seb.mspedido.application.port.in.command.stock.StockDescontadoCommand;
import com.seb.mspedido.application.port.in.query.CotizacionEnvioQuery;
import com.seb.mspedido.application.port.out.*;
import com.seb.mspedido.application.service.PedidoService;
import com.seb.mspedido.domain.model.*;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.transaction.support.TransactionSynchronizationManager;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class PedidoServiceTest {

    @Mock
    private PedidoOutputPort pedidoOutputPort;
    @Mock private ProductoCatalogoOutputPort productoCatalogoOutputPort;
    @Mock private DetalleDomainMapper detalleDomainMapper;
    @Mock private PedidoDomainMapper pedidoDomainMapper;
    @Mock private PedidoEventPubilsherPort pedidoEventPubilsherPort;
    @Mock private InventarioOutputPort inventarioOutputPort;
    @Mock private DistanciaOutputPort distanciaOutputPort;

    @InjectMocks
    private PedidoService pedidoService;

    private Pedido pedidoMock;
    private PedidoInputCommand commandMock;
    private final UUID PEDIDO_ID = UUID.randomUUID();
    private final UUID PRODUCTO_ID = UUID.randomUUID();
    private final UUID INVENTARIO_ID = UUID.randomUUID();
    private final UUID USUARIO_ID = UUID.randomUUID();

    @BeforeEach
    void setUp() {
        TransactionSynchronizationManager.initSynchronization();
        Detalle detalle = new Detalle();
        detalle.setProductoId(PRODUCTO_ID);
        detalle.setInventarioId(INVENTARIO_ID);
        detalle.setCantidad(2);
        detalle.setPrecio(new BigDecimal("1500"));

        List<Detalle> detalles = new ArrayList<>();
        detalles.add(detalle);

        Ubicacion origen = new Ubicacion();
        origen.setLatitude(new BigDecimal("-34.0"));
        origen.setLongitude(new BigDecimal("-70.0"));

        Ubicacion destino = new Ubicacion();
        destino.setLatitude(new BigDecimal("-34.1"));
        destino.setLongitude(new BigDecimal("-70.1"));
        pedidoMock = new Pedido();
        pedidoMock.setPedidoId(PEDIDO_ID);
        pedidoMock.setUsuarioId(USUARIO_ID);
        pedidoMock.setDetalles(detalles);
        pedidoMock.setDestino(destino);
        pedidoMock.setOrigen(origen);
        commandMock = mock(PedidoInputCommand.class);
    }

    @AfterEach
    void tearDown() {
        TransactionSynchronizationManager.clear();
    }

    // guardarPedido
    @Test
    void guardarPedido_Debe_Regresar_200k() {
        ProductoCache productoCache = new ProductoCache(PRODUCTO_ID, "Test", new BigDecimal("1000"));

        when(pedidoDomainMapper.toDomain(commandMock)).thenReturn(pedidoMock);
        when(productoCatalogoOutputPort.getProductoReferencia(PRODUCTO_ID)).thenReturn(Optional.of(productoCache));
        when(inventarioOutputPort.obtenerUbicacionInventario(INVENTARIO_ID)).thenReturn(pedidoMock.getOrigen());
        when(distanciaOutputPort.obtenerDistancia(any(), any(), any(), any())).thenReturn(5.0);
        when(pedidoOutputPort.guardarPedido(pedidoMock)).thenReturn(pedidoMock);

        Pedido resultado = pedidoService.guardarPedido(commandMock);

        assertNotNull(resultado);
        assertEquals(Estado.PENDIENTE, resultado.getEstado());
        assertEquals(new BigDecimal("10000"), resultado.getCostoEnvio());
        verify(pedidoOutputPort, times(1)).guardarPedido(pedidoMock);
    }

    @Test
    void guardarPedido_DebeLanzarExcepcion_CuandoDetallesEstanVacios() {
        Pedido pedidoSinDetalles = new Pedido();
        when(pedidoDomainMapper.toDomain(commandMock)).thenReturn(pedidoSinDetalles);

        assertThrows(IllegalArgumentException.class, () -> pedidoService.guardarPedido(commandMock));
    }

    @Test
    void guardarPedido_DebeLanzarExcepcion_CuandoProductoNoExisteEnCache() {
        when(pedidoDomainMapper.toDomain(commandMock)).thenReturn(pedidoMock);
        when(productoCatalogoOutputPort.getProductoReferencia(PRODUCTO_ID)).thenReturn(Optional.empty());

        RuntimeException exception = assertThrows(RuntimeException.class, () -> pedidoService.guardarPedido(commandMock));
        assertTrue(exception.getMessage().contains("No existe el producto referencia"));
    }

    // Actualizar pedido
    @Test
    void actualizarPedido_DebeActualizarYGuardar() {
        UbicacionInputCommand ubiCommandMock = mock(UbicacionInputCommand.class);
        when(commandMock.destino()).thenReturn(ubiCommandMock);

        DetalleInputCommand detalleCommandMock = mock(DetalleInputCommand.class);
        when(commandMock.detalles()).thenReturn(List.of(detalleCommandMock));

        when(pedidoOutputPort.obtenerPedido(PEDIDO_ID)).thenReturn(Optional.of(pedidoMock));
        when(pedidoDomainMapper.toDomain(any(UbicacionInputCommand.class))).thenReturn(pedidoMock.getDestino());
        when(detalleDomainMapper.toDomainList(any())).thenReturn(pedidoMock.getDetalles());
        when(inventarioOutputPort.obtenerUbicacionInventario(any())).thenReturn(pedidoMock.getOrigen());
        when(pedidoOutputPort.guardarPedido(pedidoMock)).thenReturn(pedidoMock);

        Pedido resultado = pedidoService.actualizarPedido(PEDIDO_ID, commandMock);

        assertNotNull(resultado);
        verify(pedidoOutputPort, times(1)).guardarPedido(pedidoMock);
    }

    @Test
    void actualizarPedido_DebeLanzarExcepcion_CuandoNoExiste() {
        when(pedidoOutputPort.obtenerPedido(PEDIDO_ID)).thenReturn(Optional.empty());

        assertThrows(RuntimeException.class, () -> pedidoService.actualizarPedido(PEDIDO_ID, commandMock));
    }


    // Pruebas de cambio de estado en los pedidos
    @Test
    void confirmarStockYPrepararPedido_DebeCambiarEstadoAPreparando() {
        StockDescontadoCommand stockCommand = new StockDescontadoCommand(PEDIDO_ID,true);
        when(pedidoOutputPort.obtenerPedido(PEDIDO_ID)).thenReturn(Optional.of(pedidoMock));
        when(pedidoOutputPort.guardarPedido(pedidoMock)).thenReturn(pedidoMock);

        pedidoService.confirmarStockYPrepararPedido(stockCommand);

        assertEquals(Estado.PREPARANDO, pedidoMock.getEstado());
        verify(pedidoEventPubilsherPort, times(1)).publicarPedidoPreparado(pedidoMock);
    }

    @Test
    void marcarPedidoEnviado_DebeCambiarEstadoAEnviado() {
        when(pedidoOutputPort.obtenerPedido(PEDIDO_ID)).thenReturn(Optional.of(pedidoMock));

        pedidoService.marcarPedidoEnviado(PEDIDO_ID);

        assertEquals(Estado.ENVIADO, pedidoMock.getEstado());
        verify(pedidoOutputPort, times(1)).guardarPedido(pedidoMock);
    }

    @Test
    void marcarPedidoEntregado_DebeCambiarEstadoAEntregadoYSetearFecha() {
        when(pedidoOutputPort.obtenerPedido(PEDIDO_ID)).thenReturn(Optional.of(pedidoMock));

        pedidoService.marcarPedidoEntregado(PEDIDO_ID);

        assertEquals(Estado.ENTREGADO, pedidoMock.getEstado());
        assertNotNull(pedidoMock.getFechaEntrega());
        verify(pedidoOutputPort, times(1)).guardarPedido(pedidoMock);
    }

    // Cotizacion costo de envio
    @Test
    void cotizarCostoDeEnvio_DebeAplicarTarifaMinima_CuandoDistanciaEsMuyCorta() {
        CotizacionEnvioQuery query = mock(CotizacionEnvioQuery.class);
        when(distanciaOutputPort.obtenerDistancia(any(), any(), any(), any())).thenReturn(0.5); // 0.5 * 2000 = 1000

        CotizacionEnvioResultado resultado = pedidoService.cotizarCostoDeEnvio(query);

        assertEquals(new BigDecimal("2000"), resultado.costoEnvio());
    }

    @Test
    void cotizarCostoDeEnvio_DebeCalcularNormal_CuandoSuperaMinimo() {
        CotizacionEnvioQuery query = mock(CotizacionEnvioQuery.class);
        when(distanciaOutputPort.obtenerDistancia(any(), any(), any(), any())).thenReturn(2.0); // 2.0 * 2000 = 4000

        CotizacionEnvioResultado resultado = pedidoService.cotizarCostoDeEnvio(query);

        assertEquals(new BigDecimal("4000"), resultado.costoEnvio());
    }


    // Busquedas
    @Test
    void buscarPedido_DebeDelegarBuscador_CuandoHayParametro() {
        Page<Pedido> pagina = new PageImpl<>(List.of(pedidoMock));
        when(pedidoOutputPort.buscarPedido(eq("123"), any(Pageable.class))).thenReturn(pagina);

        Page<Pedido> resultado = pedidoService.buscarPedido("123", mock(Pageable.class));

        assertEquals(1, resultado.getTotalElements());
    }

    @Test
    void buscarPedido_DebeRetornarTodos_CuandoParametroEsNulo() {
        Page<Pedido> pagina = new PageImpl<>(List.of(pedidoMock));
        when(pedidoOutputPort.obtenerPedidos(any(Pageable.class))).thenReturn(pagina);

        Page<Pedido> resultado = pedidoService.buscarPedido(null, mock(Pageable.class));

        assertEquals(1, resultado.getTotalElements());
    }

    @Test
    void obtenerPedidosPorEstado_DebeFiltrarPorEstadoEnum() {
        Page<Pedido> pagina = new PageImpl<>(List.of(pedidoMock));
        when(pedidoOutputPort.obtenerPedidosPorEstado(eq(Estado.PENDIENTE), any(Pageable.class))).thenReturn(pagina);

        Page<Pedido> resultado = pedidoService.obtenerPedidosPorEstado("PENDIENTE", mock(Pageable.class));

        assertEquals(1, resultado.getTotalElements());
    }

    @Test
    void obtenerPedidosPorEstado_DebeRetornarTodos_CuandoEstadoEsInvalido() {
        Page<Pedido> pagina = new PageImpl<>(List.of(pedidoMock));
        when(pedidoOutputPort.obtenerPedidos(any(Pageable.class))).thenReturn(pagina);

        Page<Pedido> resultado = pedidoService.obtenerPedidosPorEstado("ESTADO_INVENTADO", mock(Pageable.class));

        assertEquals(1, resultado.getTotalElements());
    }

    @Test
    void eliminarPedido_DebeLlamarAlPuerto() {
        when(pedidoOutputPort.obtenerPedido(PEDIDO_ID)).thenReturn(Optional.of(pedidoMock));
        doNothing().when(pedidoOutputPort).eliminarPedido(PEDIDO_ID);

        pedidoService.eliminarPedido(PEDIDO_ID);

        verify(pedidoOutputPort, times(1)).eliminarPedido(PEDIDO_ID);
    }

}
