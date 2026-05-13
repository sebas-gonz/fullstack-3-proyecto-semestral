package com.seb.mspedido.infrastructure.adapter.in.kafka.listener;

import com.seb.mspedido.application.port.in.PedidoInputPort;
import com.seb.mspedido.application.port.in.command.stock.StockDescontadoCommand;
import com.seb.mspedido.infrastructure.adapter.in.kafka.dto.stock.StockDescontadoEventDto;
import com.seb.mspedido.infrastructure.adapter.in.kafka.mapper.StockDescontadoKafkaMapper;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
@Slf4j
@AllArgsConstructor
public class StockDescontadoKafkaListener {
    private final PedidoInputPort  pedidoInputPort;
    private final StockDescontadoKafkaMapper stockDescontadoKafkaMapper;
    @KafkaListener(topics = "stock-descontado-topic", groupId = "ms-pedido-group")
    public void escucharRespuestaStock(StockDescontadoEventDto evento) {
        if (evento.exito()) {
            log.info("¡Confirmación recibida! El stock para el pedido {} ha sido reservado.", evento.pedidoId());
            StockDescontadoCommand command = stockDescontadoKafkaMapper.toStockDescontadoCommand(evento);
            pedidoInputPort.confirmarStockYPrepararPedido(command);
        } else {
            log.error("La bodega rechazó el pedido {} por falta de stock.", evento.pedidoId());
        }
    }
}
