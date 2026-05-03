package com.seb.msinventario.application.port.in;

import com.seb.msinventario.application.port.in.command.stock.StockInputCommand;
import com.seb.msinventario.domain.model.Stock;

import java.util.List;
import java.util.UUID;

public interface StockInputPort {
    Stock guardarStock(UUID inventarioId, StockInputCommand stockInputCommand);
    Stock actualizarStock(UUID inventarioId, UUID stockId, StockInputCommand stockInputCommand);
    Stock obtenerStock(UUID inventarioId, UUID stockId);
    List<Stock> obtenerStocks(UUID inventarioId);
    void eliminarStock(UUID inventarioId,UUID stockId);
}
