package com.seb.msinventario.infrastructure.adapter.in.kafka.listener;

import com.seb.msinventario.infrastructure.adapter.in.kafka.mapper.PedidoCreadoKafkaMapper;
import com.seb.msinventario.application.port.in.InventarioInputPort;
import com.seb.msinventario.application.port.in.command.stock.DescontarStockCommand;
import com.seb.msinventario.infrastructure.adapter.in.kafka.dto.pedido.PedidoCreadoEventDto;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
@Slf4j
@AllArgsConstructor
public class PedidoCreadoKafkaListener {
    private final InventarioInputPort  inventarioInputPort;
    private final PedidoCreadoKafkaMapper  pedidoCreadoKafkaMapper;
    @KafkaListener(topics = "pedidos-creados-topic", groupId = "ms-inventario-group")
    public void escucharPedidoCreado(PedidoCreadoEventDto evento) {
        log.info("¡ALERTA DE BODEGA! Se recibió solicitud de stock para el pedido: {}", evento.pedidoId());
        try {
            DescontarStockCommand descontarStockCommand = pedidoCreadoKafkaMapper.toCommand(evento);
            inventarioInputPort.procesarDescuentoStock(descontarStockCommand);
        } catch (Exception e) {
            log.error("Error crítico procesando el stock para el pedido {}: {}", evento.pedidoId(), e.getMessage());
        }
    }
}
