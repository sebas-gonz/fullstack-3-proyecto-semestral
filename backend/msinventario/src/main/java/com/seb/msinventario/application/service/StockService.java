package com.seb.msinventario.application.service;

import com.seb.msinventario.application.exception.InventoryNotFoundException;
import com.seb.msinventario.application.exception.StockNotFoundException;
import com.seb.msinventario.application.mapper.StockDomainMapper;
import com.seb.msinventario.application.port.in.StockInputPort;
import com.seb.msinventario.application.port.in.command.stock.ProductoStockCommand;
import com.seb.msinventario.application.port.in.command.stock.StockInputCommand;
import com.seb.msinventario.application.port.out.InventarioEventPublisherPort;
import com.seb.msinventario.application.port.out.InventarioOutputPort;
import com.seb.msinventario.application.port.out.StockOutputPort;
import com.seb.msinventario.domain.model.Inventario;
import com.seb.msinventario.domain.model.Stock;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;


@Service
@Slf4j
@AllArgsConstructor
public class StockService implements StockInputPort {
    private final InventarioOutputPort inventarioOutputPort;
    private final StockDomainMapper  stockDomainMapper;
    private final InventarioEventPublisherPort inventarioEventPublisherPort;
    private final StockOutputPort  stockOutputPort;
    @Override
    @Transactional
    public Stock guardarStock(UUID inventarioId,StockInputCommand stockInputCommand) {
        Stock stock = stockDomainMapper.toDomain(stockInputCommand);
        Inventario inventario = inventarioOutputPort.obtenerInventario(inventarioId)
                .orElseThrow(
                        () -> new InventoryNotFoundException(inventarioId)
                );
        inventario.getStocks().add(stock);
        Inventario guardado = inventarioOutputPort.guardarInventario(inventario);
        publicarStockTotalProducto(stock.getProductoId());
        return guardado.getStocks().stream().
                filter(s -> s.getStockId().equals(stock.getStockId())).findFirst().orElseThrow(
                        () -> new StockNotFoundException(stock.getStockId())
                );
    }

    @Override
    @Transactional
    public Stock actualizarStock(UUID inventarioId,UUID stockId, StockInputCommand stockInputCommand) {

        Inventario inventario = inventarioOutputPort.obtenerInventario(inventarioId).orElseThrow(
                () -> new InventoryNotFoundException(inventarioId)
        );
        Stock actualizado = inventario.getStocks().stream().filter(stock -> stock.getStockId().equals(stockId)
        ).findFirst().map( stock -> {
            stock.setLote(stockInputCommand.lote());
            stock.setCantidad(stockInputCommand.cantidad());
            stock.setProductoId(stockInputCommand.productoId());
            stock.setFechaRegistroLote(stockInputCommand.fechaRegistroLote());
            return stock;
        }).orElseThrow(
                ()-> new StockNotFoundException(stockId)
        );
        Inventario guardado = inventarioOutputPort.guardarInventario(inventario);
        publicarStockTotalProducto(actualizado.getProductoId());
        return guardado.getStocks()
                .stream()
                .filter(s -> s.getStockId().equals(stockId)).findFirst().orElseThrow(
                () -> new StockNotFoundException(stockId)
        );
    }

    @Override
    public Stock obtenerStock(UUID inventarioId, UUID stockId) {
        Inventario inventario = inventarioOutputPort.obtenerInventario(inventarioId).orElseThrow(
                () -> new InventoryNotFoundException(inventarioId)
        );
        return inventario.getStocks().stream().filter(s -> s.getStockId().equals(stockId)).findFirst().orElseThrow(
                () -> new StockNotFoundException(stockId)
        );
    }

    @Override
    public List<Stock> obtenerStocks(UUID inventarioId) {
        Inventario inventario = inventarioOutputPort.obtenerInventario(inventarioId).orElseThrow(
                () -> new InventoryNotFoundException(inventarioId)
        );
        return inventario.getStocks();
    }

    @Override
    @Transactional
    public void eliminarStock(UUID inventarioId,UUID stockId) {
        Inventario inventario = inventarioOutputPort.obtenerInventario(inventarioId).orElseThrow(
                () -> new InventoryNotFoundException(inventarioId)
        );
        Stock eliminado = inventario.getStocks().stream().filter(s -> s.getStockId().equals(stockId)).findFirst().orElseThrow(
                () -> new StockNotFoundException(stockId)
        );
        UUID productoId = eliminado.getProductoId();
        inventarioOutputPort.guardarInventario(inventario);
        publicarStockTotalProducto(productoId);
    }
    private void publicarStockTotalProducto(UUID productoId) {
        List<Stock> stocks = stockOutputPort.obtenerStocksPorProducto(productoId);
        Integer cantidadTotal = stocks.stream().mapToInt(Stock::getCantidad).sum();
        inventarioEventPublisherPort.publicarStockTotal(new ProductoStockCommand(productoId, cantidadTotal));
    }
}
