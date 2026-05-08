package com.seb.mspedido.infrastructure.adapter.in.kafka.listener;

import com.seb.mspedido.application.port.in.PedidoInputPort;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
@Slf4j
@AllArgsConstructor
public class EnvioEntregadoListener {
    private final PedidoInputPort pedidoInputPort;
    @KafkaListener(topics = "envio-entregado-topic", groupId = "ms-pedido-entrega-group")
    public void escucharEnvioEnRuta(UUID pedidoId) {
        log.info("Recibida notificación de entrega para el pedido: {}", pedidoId);
        pedidoInputPort.marcarPedidoEntregado(pedidoId);
    }
}
