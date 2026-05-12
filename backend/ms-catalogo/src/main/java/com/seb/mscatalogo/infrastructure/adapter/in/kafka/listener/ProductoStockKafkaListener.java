package com.seb.mscatalogo.infrastructure.adapter.in.kafka.listener;

import com.seb.mscatalogo.application.port.in.ProductoInputPort;
import com.seb.mscatalogo.application.port.in.command.producto.ActualizarStockProductoCommand;
import com.seb.mscatalogo.infrastructure.adapter.in.kafka.dto.ProductoStockEventDto;
import com.seb.mscatalogo.infrastructure.adapter.in.kafka.mapper.ProductoStockKafkaMapper;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
@Slf4j
@AllArgsConstructor
public class ProductoStockKafkaListener {
    private final ProductoInputPort productoInputPort;
    private final ProductoStockKafkaMapper productoStockKafkaMapper;
    @KafkaListener(topics = "inventario-stock-topic", groupId = "ms-catalogo-group")
    public void escucharStockActualizado(ProductoStockEventDto eventDto) {
        log.info("Iniciando escucharStockActualizado con cantidad de stock {} para el producto {}", eventDto.cantidadTotal(), eventDto.productoId());
        ActualizarStockProductoCommand command = productoStockKafkaMapper.toCommand(eventDto);
        productoInputPort.actualizarCantidadProducto(command);
    }
}
