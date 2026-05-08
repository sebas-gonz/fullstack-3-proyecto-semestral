package com.seb.msinventario.application.service;

import com.seb.msinventario.application.exception.InventoryNotFoundException;
import com.seb.msinventario.application.mapper.InventarioDomainMapper;
import com.seb.msinventario.application.port.in.InventarioInputPort;
import com.seb.msinventario.application.port.in.command.inventario.InventarioInputCommand;
import com.seb.msinventario.application.port.in.command.stock.DescontarStockCommand;
import com.seb.msinventario.application.port.out.InventarioEventPublisherPort;
import com.seb.msinventario.application.port.out.InventarioOutputPort;
import com.seb.msinventario.application.port.out.StockOutputPort;
import com.seb.msinventario.domain.model.Inventario;
import com.seb.msinventario.domain.model.Stock;
import com.seb.msinventario.domain.model.Ubicacion;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.UUID;

@Service
@Slf4j
@AllArgsConstructor
public class InventarioService implements InventarioInputPort {
    private InventarioOutputPort inventarioOutputPort;
    private InventarioDomainMapper inventarioDomainMapper;
    private StockOutputPort  stockOutputPort;
    private InventarioEventPublisherPort inventarioEventPublisherPort;
    @Override
    public Inventario guardarInventario(InventarioInputCommand inventarioInputCommand) {
        Inventario inventario = inventarioDomainMapper.toDomain(inventarioInputCommand);
        return inventarioOutputPort.guardarInventario(inventario);
    }

    @Override
    public Inventario guardarInventario(Inventario inventario) {
        return inventarioOutputPort.guardarInventario(inventario);
    }
    @Transactional
    @Override
    public Inventario actualizarInventario(UUID id, InventarioInputCommand inventarioInputCommand) {
        Inventario inventarioOptional = inventarioOutputPort.obtenerInventario(id).orElseThrow(
                () -> new InventoryNotFoundException(id)
        );
        Ubicacion ubicacion = inventarioDomainMapper.toDomain(inventarioInputCommand.ubicacion());
        inventarioOptional.setNombre(inventarioInputCommand.nombre());
        inventarioOptional.setUbicacion(ubicacion);
        return inventarioOutputPort.guardarInventario(inventarioOptional);
    }

    @Override
    public Inventario obtenerInventario(UUID inventarioId) {
        return inventarioOutputPort.obtenerInventario(inventarioId).orElseThrow(
                () -> new InventoryNotFoundException(inventarioId)
        );
    }

    @Override
    public List<Inventario> obtenerInventarios() {
        return inventarioOutputPort.obtenerInventarios();
    }

    @Override
    public void eliminarInventario(UUID inventarioId) {
        Inventario inventario = inventarioOutputPort.obtenerInventario(inventarioId).orElseThrow(
                () -> new InventoryNotFoundException(inventarioId)
        );
        inventarioOutputPort.eliminarInventario(inventarioId);
    }

    @Override
    @Transactional
    public void procesarDescuentoStock(DescontarStockCommand descontarStockCommand) {
        descontarStockCommand.productosPedidos().forEach(pedido -> {
            int cantidadNececitada = pedido.cantidad();
            List<Stock> stocks = stockOutputPort.obtenerStockPorInventarioYProducto(pedido.inventarioId(), pedido.productoId());
            for(Stock stock : stocks){
                System.out.println(stock.getCantidad()+ " " + stock.getStockId());
                if (cantidadNececitada == 0) break;
                if (stock.getCantidad() >= cantidadNececitada){
                    stock.setCantidad(stock.getCantidad() - cantidadNececitada);
                    cantidadNececitada = 0;
                } else {
                    cantidadNececitada -= stock.getCantidad();
                    stock.setCantidad(0);

                }
            }
            stockOutputPort.guardarStocks(stocks);
        });
        inventarioEventPublisherPort.publicarRespuestaStock(descontarStockCommand.pedidoId(), true);
        ;
    }
}
