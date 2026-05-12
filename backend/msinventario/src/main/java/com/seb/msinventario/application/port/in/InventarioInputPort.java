package com.seb.msinventario.application.port.in;

import com.seb.msinventario.application.port.in.command.inventario.InventarioInputCommand;
import com.seb.msinventario.application.port.in.command.stock.DescontarStockCommand;
import com.seb.msinventario.domain.model.Inventario;

import java.util.List;
import java.util.UUID;

public interface InventarioInputPort {
    Inventario guardarInventario(InventarioInputCommand inventarioInputCommand);
    Inventario guardarInventario(Inventario inventario);
    Inventario actualizarInventario(UUID id, InventarioInputCommand inventarioInputCommand);
    Inventario obtenerInventario(UUID inventarioId);
    List<Inventario> obtenerInventarios();
    void eliminarInventario(UUID inventarioId);
    void procesarDescuentoStock(DescontarStockCommand  descontarStockCommand);
    List<Inventario> obtenerInventariosPorProducto(UUID productoId);

}
