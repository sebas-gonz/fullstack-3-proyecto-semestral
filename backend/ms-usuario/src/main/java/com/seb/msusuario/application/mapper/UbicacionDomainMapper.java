package com.seb.msusuario.application.mapper;

import com.seb.msusuario.application.port.in.command.CrearUbicacionCommand;
import com.seb.msusuario.domain.model.Ubicacion;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;

import java.util.List;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface UbicacionDomainMapper {
    Ubicacion toDomain(CrearUbicacionCommand command);
    List<Ubicacion> toDomain(List<CrearUbicacionCommand> commands);
}
