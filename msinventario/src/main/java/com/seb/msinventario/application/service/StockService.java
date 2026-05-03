package com.seb.msinventario.application.service;

import com.seb.msinventario.application.exception.InventoryNotFoundException;
import com.seb.msinventario.application.exception.StockNotFoundException;
import com.seb.msinventario.application.port.in.StockInputPort;
import com.seb.msinventario.application.port.in.command.mapper.StockCommandMapper;
import com.seb.msinventario.application.port.in.command.stock.StockInputCommand;
import com.seb.msinventario.application.port.out.InventarioOutputPort;
import com.seb.msinventario.domain.model.Inventario;
import com.seb.msinventario.domain.model.Stock;
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
    private final StockCommandMapper stockCommandMapper;
    @Override
    public Stock guardarStock(UUID inventarioId,StockInputCommand stockInputCommand) {
        Stock stock = stockCommandMapper.toDomain(stockInputCommand);
        Inventario inventario = inventarioOutputPort.obtenerInventario(inventarioId)
                .orElseThrow(
                        () -> new InventoryNotFoundException(inventarioId)
                );
        inventario.getStocks().add(stock);
        return inventarioOutputPort.guardarInventario(inventario).getStocks().stream().
                filter(s -> s.getStockId().equals(stock.getStockId())).findFirst().orElseThrow(
                        () -> new StockNotFoundException(stock.getStockId())
                );
    }

    @Override
    public Stock actualizarStock(UUID inventarioId,UUID stockId, StockInputCommand stockInputCommand) {

        Inventario inventario = inventarioOutputPort.obtenerInventario(inventarioId).orElseThrow(
                () -> new InventoryNotFoundException(inventarioId)
        );
        inventario.getStocks().stream().filter(stock -> stock.getStockId().equals(stockId)
        ).findFirst().map( stock -> {
            stock.setLote(stockInputCommand.lote());
            stock.setCantidad(stockInputCommand.cantidad());
            stock.setProductoId(stockInputCommand.productoId());
            stock.setFechaRegistroLote(stockInputCommand.fechaRegistroLote());
            return stock;
        }).orElseThrow(
                ()-> new StockNotFoundException(stockId)
        );
        return inventarioOutputPort.guardarInventario(inventario)
                .getStocks().stream().
                filter(s -> s.getStockId().equals(stockId)).findFirst().orElseThrow(
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
    public void eliminarStock(UUID inventarioId,UUID stockId) {
        Inventario inventario = inventarioOutputPort.obtenerInventario(inventarioId).orElseThrow(
                () -> new InventoryNotFoundException(inventarioId)
        );
        boolean stockExiste = inventario.getStocks().removeIf(s -> s.getStockId().equals(stockId));
        if (!stockExiste) {
            throw new InventoryNotFoundException(stockId);
        }
        inventarioOutputPort.guardarInventario(inventario);
    }
}
