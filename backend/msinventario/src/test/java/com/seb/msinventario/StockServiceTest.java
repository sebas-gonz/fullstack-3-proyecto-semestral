package com.seb.msinventario;

import com.seb.msinventario.application.exception.InventoryNotFoundException;
import com.seb.msinventario.application.exception.StockNotFoundException;
import com.seb.msinventario.application.mapper.StockDomainMapper;
import com.seb.msinventario.application.port.in.command.stock.StockInputCommand;
import com.seb.msinventario.application.port.out.InventarioEventPublisherPort;
import com.seb.msinventario.application.port.out.InventarioOutputPort;
import com.seb.msinventario.application.port.out.StockOutputPort;
import com.seb.msinventario.application.service.StockService;
import com.seb.msinventario.domain.model.Inventario;
import com.seb.msinventario.domain.model.Stock;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class StockServiceTest {
    @Mock
    private InventarioOutputPort inventarioOutputPort;
    @Mock private StockDomainMapper stockDomainMapper;
    @Mock private InventarioEventPublisherPort inventarioEventPublisherPort;
    @Mock private StockOutputPort stockOutputPort;

    @InjectMocks
    private StockService stockService;

    private Inventario inventarioMock;
    private Stock stockMock;
    private StockInputCommand commandMock;

    private final UUID INVENTARIO_ID = UUID.randomUUID();
    private final UUID STOCK_ID = UUID.randomUUID();
    private final UUID PRODUCTO_ID = UUID.randomUUID();

    @BeforeEach
    void setUp() {
        stockMock = new Stock();
        stockMock.setStockId(STOCK_ID);
        stockMock.setProductoId(PRODUCTO_ID);
        stockMock.setCantidad(50);
        stockMock.setLote("LOTE-TEST");
        List<Stock> listaStocks = new ArrayList<>();
        listaStocks.add(stockMock);

        inventarioMock = new Inventario();
        inventarioMock.setStocks(listaStocks);

        commandMock = new StockInputCommand(PRODUCTO_ID, "LOTE-NUEVO", 100, Instant.now());
    }

    // Guardar Stock
    @Test
    void guardarStock_DebeAgregarYPublicarEvento() {
        Stock nuevoStock = new Stock();
        nuevoStock.setStockId(UUID.randomUUID());
        nuevoStock.setProductoId(PRODUCTO_ID);
        nuevoStock.setCantidad(100);

        when(stockDomainMapper.toDomain(commandMock)).thenReturn(nuevoStock);
        when(inventarioOutputPort.obtenerInventario(INVENTARIO_ID)).thenReturn(Optional.of(inventarioMock));
        when(inventarioOutputPort.guardarInventario(inventarioMock)).thenReturn(inventarioMock);
        when(stockOutputPort.obtenerStocksPorProducto(PRODUCTO_ID)).thenReturn(inventarioMock.getStocks());

        Stock resultado = stockService.guardarStock(INVENTARIO_ID, commandMock);

        assertNotNull(resultado);
        assertEquals(nuevoStock.getStockId(), resultado.getStockId());
        assertEquals(2, inventarioMock.getStocks().size());
        verify(inventarioEventPublisherPort, times(1)).publicarStockTotal(any());
    }

    @Test
    void guardarStock_DebeLanzarExcepcion_CuandoInventarioNoExiste() {
        when(stockDomainMapper.toDomain(commandMock)).thenReturn(new Stock());
        when(inventarioOutputPort.obtenerInventario(INVENTARIO_ID)).thenReturn(Optional.empty());

        assertThrows(InventoryNotFoundException.class, () -> stockService.guardarStock(INVENTARIO_ID, commandMock));
    }

    //Actualizar Stock
    @Test
    void actualizarStock_DebeModificarYPublicarEvento() {
        when(inventarioOutputPort.obtenerInventario(INVENTARIO_ID)).thenReturn(Optional.of(inventarioMock));
        when(inventarioOutputPort.guardarInventario(inventarioMock)).thenReturn(inventarioMock);
        when(stockOutputPort.obtenerStocksPorProducto(PRODUCTO_ID)).thenReturn(inventarioMock.getStocks());

        Stock resultado = stockService.actualizarStock(INVENTARIO_ID, STOCK_ID, commandMock);

        assertNotNull(resultado);
        assertEquals("LOTE-NUEVO", resultado.getLote());
        assertEquals(100, resultado.getCantidad());
        verify(inventarioOutputPort, times(1)).guardarInventario(inventarioMock);
        verify(inventarioEventPublisherPort, times(1)).publicarStockTotal(any());
    }

    @Test
    void actualizarStock_DebeLanzarExcepcion_CuandoStockNoExisteEnInventario() {
        when(inventarioOutputPort.obtenerInventario(INVENTARIO_ID)).thenReturn(Optional.of(inventarioMock));

        UUID stockIdFalso = UUID.randomUUID();

        assertThrows(StockNotFoundException.class, () -> stockService.actualizarStock(INVENTARIO_ID, stockIdFalso, commandMock));
    }

    //Obtener stock
    @Test
    void obtenerStock_DebeRetornarStockEspecifico() {
        when(inventarioOutputPort.obtenerInventario(INVENTARIO_ID)).thenReturn(Optional.of(inventarioMock));

        Stock resultado = stockService.obtenerStock(INVENTARIO_ID, STOCK_ID);

        assertNotNull(resultado);
        assertEquals(STOCK_ID, resultado.getStockId());
    }

    @Test
    void obtenerStocks_DebeRetornarListaCompleta() {
        when(inventarioOutputPort.obtenerInventario(INVENTARIO_ID)).thenReturn(Optional.of(inventarioMock));

        List<Stock> resultado = stockService.obtenerStocks(INVENTARIO_ID);

        assertFalse(resultado.isEmpty());
        assertEquals(1, resultado.size());
    }

    //Eliminar stock
    @Test
    void eliminarStock_DebeRemoverYPublicarEvento() {
        when(inventarioOutputPort.obtenerInventario(INVENTARIO_ID)).thenReturn(Optional.of(inventarioMock));
        when(stockOutputPort.obtenerStocksPorProducto(PRODUCTO_ID)).thenReturn(new ArrayList<>()); // Simula que ya no hay stocks de ese producto

        stockService.eliminarStock(INVENTARIO_ID, STOCK_ID);
        assertTrue(inventarioMock.getStocks().isEmpty());

        verify(inventarioOutputPort, times(1)).guardarInventario(inventarioMock);
        verify(inventarioEventPublisherPort, times(1)).publicarStockTotal(any());
    }

    @Test
    void eliminarStock_DebeLanzarExcepcion_CuandoStockNoExiste() {
        when(inventarioOutputPort.obtenerInventario(INVENTARIO_ID)).thenReturn(Optional.of(inventarioMock));

        UUID stockIdFalso = UUID.randomUUID();

        assertThrows(StockNotFoundException.class, () -> stockService.eliminarStock(INVENTARIO_ID, stockIdFalso));
        verify(inventarioOutputPort, never()).guardarInventario(any());
    }
}
