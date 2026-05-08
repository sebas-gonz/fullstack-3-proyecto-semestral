package com.seb.mspedido.infrastructure.adapter.in.kafka.listener;

import com.seb.mspedido.application.port.in.PedidoInputPort;
import com.seb.mspedido.infrastructure.adapter.in.kafka.dto.pedido.PedidoEnviadoEventDto;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
@Slf4j
@AllArgsConstructor
public class EnvioEnRutaListener {
    private final PedidoInputPort  pedidoInputPort;
    @KafkaListener(topics = "envio-en-ruta-topic", groupId = "ms-pedido-envio-group")
    public void escucharEnvioEnRuta(PedidoEnviadoEventDto  pedidoEnviadoEventDto) {
        log.info("Recibida notificación de envío en ruta para el pedido: {}", pedidoEnviadoEventDto.pedidoId());
        pedidoInputPort.marcarPedidoEnviado(pedidoEnviadoEventDto.pedidoId());
    }
}
