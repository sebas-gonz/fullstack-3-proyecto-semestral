package com.seb.msusuario.infrastructure.adapter.in.web.mapper;

import com.seb.msusuario.domain.model.Ubicacion;
import com.seb.msusuario.infrastructure.adapter.in.web.dto.direccion.DireccionRequest;
import com.seb.msusuario.infrastructure.adapter.in.web.dto.direccion.DireccionResponse;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;

import java.util.List;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING )
public interface DireccionDtoMapper{
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "fechaRegistro", ignore = true)
    @Mapping(target = "fechaModificacion", ignore = true)
    @Mapping(target = "latitude", ignore = true)
    @Mapping(target = "longitude", ignore = true)
    Ubicacion toDomain(DireccionRequest direccionRequest);
    DireccionResponse toResponse(Ubicacion ubicacion);
    List<DireccionResponse> toResponseList(List<Ubicacion> direcciones);
    List<Ubicacion> toDomainList(List<DireccionResponse> direccionResponses);

}
