package com.seb.msinventario;

import com.seb.msinventario.application.exception.InventoryNotFoundException;
import com.seb.msinventario.application.mapper.InventarioDomainMapper;
import com.seb.msinventario.application.port.in.command.inventario.InventarioInputCommand;
import com.seb.msinventario.application.port.in.command.producto.ProductoPedidoCommand;
import com.seb.msinventario.application.port.in.command.stock.DescontarStockCommand;
import com.seb.msinventario.application.port.out.InventarioEventPublisherPort;
import com.seb.msinventario.application.port.out.InventarioOutputPort;
import com.seb.msinventario.application.port.out.StockOutputPort;
import com.seb.msinventario.application.service.InventarioService;
import com.seb.msinventario.domain.model.Inventario;
import com.seb.msinventario.domain.model.Stock;
import com.seb.msinventario.domain.model.Ubicacion;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class InventarioServiceTest {
    @Mock
    private InventarioOutputPort inventarioOutputPort;
    @Mock private InventarioDomainMapper inventarioDomainMapper;
    @Mock private StockOutputPort stockOutputPort;
    @Mock private InventarioEventPublisherPort inventarioEventPublisherPort;

    @InjectMocks
    private InventarioService inventarioService;

    private Inventario inventarioMock;
    private InventarioInputCommand commandMock;

    private final UUID INVENTARIO_ID = UUID.randomUUID();
    private final UUID PRODUCTO_ID = UUID.randomUUID();

    @BeforeEach
    void setUp() {
        inventarioMock = new Inventario();
        inventarioMock.setNombre("Bodega Principal");

        commandMock = mock(InventarioInputCommand.class);
    }

    // Guardar inventario
    @Test
    void guardarInventario_ConCommand_DebeRetornarInventario() {
        when(inventarioDomainMapper.toDomain(commandMock)).thenReturn(inventarioMock);
        when(inventarioOutputPort.guardarInventario(inventarioMock)).thenReturn(inventarioMock);

        Inventario resultado = inventarioService.guardarInventario(commandMock);

        assertNotNull(resultado);
        assertEquals("Bodega Principal", resultado.getNombre());
        verify(inventarioOutputPort, times(1)).guardarInventario(inventarioMock);
    }

    @Test
    void guardarInventario_ConEntidadDirecta_DebeRetornarInventario() {
        when(inventarioOutputPort.guardarInventario(inventarioMock)).thenReturn(inventarioMock);

        Inventario resultado = inventarioService.guardarInventario(inventarioMock);

        assertNotNull(resultado);
        verify(inventarioOutputPort, times(1)).guardarInventario(inventarioMock);
    }


    // Actualizar inventario
    @Test
    void actualizarInventario_DebeModificarYGuardarExitosamente() {
        Ubicacion ubicacionMock = new Ubicacion();
        when(inventarioOutputPort.obtenerInventario(INVENTARIO_ID)).thenReturn(Optional.of(inventarioMock));
        when(commandMock.ubicacion()).thenReturn(mock(com.seb.msinventario.application.port.in.command.inventario.UbicacionInputCommand.class));
        when(inventarioDomainMapper.toDomain(any(com.seb.msinventario.application.port.in.command.inventario.UbicacionInputCommand.class))).thenReturn(ubicacionMock);
        when(commandMock.nombre()).thenReturn("Bodega Actualizada");
        when(inventarioOutputPort.guardarInventario(inventarioMock)).thenReturn(inventarioMock);

        Inventario resultado = inventarioService.actualizarInventario(INVENTARIO_ID, commandMock);

        assertNotNull(resultado);
        assertEquals("Bodega Actualizada", inventarioMock.getNombre());
        verify(inventarioOutputPort, times(1)).guardarInventario(inventarioMock);
    }

    @Test
    void actualizarInventario_DebeLanzarExcepcion_CuandoNoExiste() {
        when(inventarioOutputPort.obtenerInventario(INVENTARIO_ID)).thenReturn(Optional.empty());

        assertThrows(InventoryNotFoundException.class, () -> inventarioService.actualizarInventario(INVENTARIO_ID, commandMock));
    }

    // Obtener y eliminar un inventario
    @Test
    void obtenerInventario_DebeRetornarInventario_CuandoExiste() {
        when(inventarioOutputPort.obtenerInventario(INVENTARIO_ID)).thenReturn(Optional.of(inventarioMock));

        Inventario resultado = inventarioService.obtenerInventario(INVENTARIO_ID);

        assertNotNull(resultado);
        assertEquals("Bodega Principal", resultado.getNombre());
    }

    @Test
    void obtenerInventario_DebeLanzarExcepcion_CuandoNoExiste() {
        when(inventarioOutputPort.obtenerInventario(INVENTARIO_ID)).thenReturn(Optional.empty());

        assertThrows(InventoryNotFoundException.class, () -> inventarioService.obtenerInventario(INVENTARIO_ID));
    }

    @Test
    void eliminarInventario_DebeProceder_CuandoExiste() {
        when(inventarioOutputPort.obtenerInventario(INVENTARIO_ID)).thenReturn(Optional.of(inventarioMock));
        doNothing().when(inventarioOutputPort).eliminarInventario(INVENTARIO_ID);

        inventarioService.eliminarInventario(INVENTARIO_ID);

        verify(inventarioOutputPort, times(1)).eliminarInventario(INVENTARIO_ID);
    }

    @Test
    void eliminarInventario_DebeLanzarExcepcion_CuandoNoExiste() {
        when(inventarioOutputPort.obtenerInventario(INVENTARIO_ID)).thenReturn(Optional.empty());

        assertThrows(InventoryNotFoundException.class, () -> inventarioService.eliminarInventario(INVENTARIO_ID));
    }

    // Procesamiento de descuento de stock por lotes
    @Test
    void procesarDescuentoStock_SuficienteStockEnPrimerLote_DebeDescontarYCompletar() {
        DescontarStockCommand descontarCommand = mock(DescontarStockCommand.class);
        var itemPedidoMock = mock(ProductoPedidoCommand.class);

        when(descontarCommand.productosPedidos()).thenReturn(List.of(itemPedidoMock));
        when(itemPedidoMock.cantidad()).thenReturn(10);
        when(itemPedidoMock.inventarioId()).thenReturn(INVENTARIO_ID);
        when(itemPedidoMock.productoId()).thenReturn(PRODUCTO_ID);

        Stock lote1 = Stock.builder().stockId(UUID.randomUUID()).cantidad(50).build();
        List<Stock> listaStocks = List.of(lote1);

        when(stockOutputPort.obtenerStockPorInventarioYProducto(INVENTARIO_ID, PRODUCTO_ID)).thenReturn(listaStocks);

        inventarioService.procesarDescuentoStock(descontarCommand);

        assertEquals(40, lote1.getCantidad());
        verify(stockOutputPort, times(1)).guardarStocks(listaStocks);
        verify(inventarioEventPublisherPort, times(1)).publicarStockTotal(any());
        verify(inventarioEventPublisherPort, times(1)).publicarRespuestaStock(any(), eq(true));
    }

    @Test
    void procesarDescuentoStock_MultiplesLotesNecesarios_DebeDrenarLotesSecuencialmente() {
        DescontarStockCommand descontarCommand = mock(DescontarStockCommand.class);
        var itemPedidoMock = mock(ProductoPedidoCommand.class);

        when(descontarCommand.productosPedidos()).thenReturn(List.of(itemPedidoMock));
        when(itemPedidoMock.cantidad()).thenReturn(35);
        when(itemPedidoMock.inventarioId()).thenReturn(INVENTARIO_ID);
        when(itemPedidoMock.productoId()).thenReturn(PRODUCTO_ID);

        Stock lote1 = Stock.builder().stockId(UUID.randomUUID()).cantidad(20).build();
        Stock lote2 = Stock.builder().stockId(UUID.randomUUID()).cantidad(30).build();
        List<Stock> listaStocks = List.of(lote1, lote2);

        when(stockOutputPort.obtenerStockPorInventarioYProducto(INVENTARIO_ID, PRODUCTO_ID)).thenReturn(listaStocks);

        inventarioService.procesarDescuentoStock(descontarCommand);

        assertEquals(0, lote1.getCantidad());
        assertEquals(15, lote2.getCantidad());

        verify(stockOutputPort, times(1)).guardarStocks(listaStocks);
        verify(inventarioEventPublisherPort, times(1)).publicarStockTotal(any());
    }

    @Test
    void obtenerInventariosPorProducto_DebeRetornarLista() {
        when(inventarioOutputPort.obtenerInventarioPorProducto(PRODUCTO_ID)).thenReturn(List.of(inventarioMock));

        List<Inventario> resultado = inventarioService.obtenerInventariosPorProducto(PRODUCTO_ID);

        assertFalse(resultado.isEmpty());
        assertEquals(1, resultado.size());
    }
}
