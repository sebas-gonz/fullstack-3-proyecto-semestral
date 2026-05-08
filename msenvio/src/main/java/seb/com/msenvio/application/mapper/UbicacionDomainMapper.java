package seb.com.msenvio.application.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;
import seb.com.msenvio.application.port.in.command.ubicacion.UbicacionCommand;
import seb.com.msenvio.domain.model.Ubicacion;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface UbicacionDomainMapper {
    Ubicacion toDomain(UbicacionCommand command);
}
