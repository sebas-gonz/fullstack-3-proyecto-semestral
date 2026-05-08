package seb.com.msenvio.infrastructure.adapter.in.kafka.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;
import seb.com.msenvio.application.port.in.command.envio.CrearEnvioCommand;
import seb.com.msenvio.infrastructure.adapter.in.kafka.dto.pedido.PedidoPreparadoEventDto;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface KafkaEventMapper {
    CrearEnvioCommand toCommand(PedidoPreparadoEventDto event);

}
