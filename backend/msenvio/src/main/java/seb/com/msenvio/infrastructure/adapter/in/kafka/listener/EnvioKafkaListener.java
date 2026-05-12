package seb.com.msenvio.infrastructure.adapter.in.kafka.listener;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;
import seb.com.msenvio.application.port.in.EnvioInputPort;
import seb.com.msenvio.application.port.in.command.envio.CrearEnvioCommand;
import seb.com.msenvio.infrastructure.adapter.in.kafka.dto.pedido.PedidoPreparadoEventDto;
import seb.com.msenvio.infrastructure.adapter.in.kafka.mapper.KafkaEventMapper;

@Component
@Slf4j
@AllArgsConstructor
public class EnvioKafkaListener {
    private final EnvioInputPort envioInputPort;
    private final KafkaEventMapper kafkaEventMapper;

    @KafkaListener(topics = "pedidos-preparados-topic", groupId = "ms-envio-group")
    public void escucharPedidoPreparado(PedidoPreparadoEventDto event) {
        log.info("Mensaje recibido de Kafka para el pedido: {}", event.pedidoId());


        CrearEnvioCommand envioCommand = kafkaEventMapper.toCommand(event);
        envioInputPort.crearEnvio(envioCommand);

        log.info("Envío creado exitosamente y disponible para repartidores.");
    }
}
