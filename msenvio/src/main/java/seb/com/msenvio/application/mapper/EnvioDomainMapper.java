package seb.com.msenvio.application.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;
import seb.com.msenvio.application.port.in.command.envio.CrearEnvioCommand;
import seb.com.msenvio.domain.model.Envio;

import java.util.List;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface EnvioDomainMapper {
    Envio toDomain(CrearEnvioCommand command);
    List<Envio> toDomainList(List<CrearEnvioCommand> commands);

}
