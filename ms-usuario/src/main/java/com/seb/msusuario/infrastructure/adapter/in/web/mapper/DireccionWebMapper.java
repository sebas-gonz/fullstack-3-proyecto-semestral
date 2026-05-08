package com.seb.msusuario.infrastructure.adapter.in.web.mapper;

import com.seb.msusuario.application.port.in.command.CrearUbicacionCommand;
import com.seb.msusuario.domain.model.Ubicacion;
import com.seb.msusuario.infrastructure.adapter.in.web.dto.direccion.DireccionRequest;
import com.seb.msusuario.infrastructure.adapter.in.web.dto.direccion.DireccionResponse;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;

import java.util.List;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING )
public interface DireccionWebMapper {

    CrearUbicacionCommand toCommand(DireccionRequest direccionRequest);
    DireccionResponse toResponse(Ubicacion ubicacion);
    List<DireccionResponse> toResponseList(List<Ubicacion> direcciones);

}
