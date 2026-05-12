package com.seb.mspedido.infrastructure.adapter.in.kafka.listener;

import com.seb.mspedido.application.port.in.command.producto.ProductoActualizadoCommand;
import com.seb.mspedido.application.port.in.ActualizarProductoInputPort;
import com.seb.mspedido.infrastructure.adapter.in.kafka.dto.producto.ProductoPrecioEventDto;
import com.seb.mspedido.infrastructure.adapter.in.kafka.mapper.ProductoKafkaMapper;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
@AllArgsConstructor
@Slf4j
public class ProductoKafkaListener {
    private final ActualizarProductoInputPort actualizarProductoInputPort;
    private final ProductoKafkaMapper productoKafkaMapper;
    @KafkaListener(topics = "catalogo-productos-topic", groupId = "ms-pedido-group")
    public void escucharCambioPrecio(ProductoPrecioEventDto event) {
        log.info("Sincronizando producto desde catálogo: {}", event.productoId());
        ProductoActualizadoCommand productoActualizado = productoKafkaMapper.toCommand(event);
        actualizarProductoInputPort.actualizarProducto(productoActualizado);
    }
}
