package com.seb.msusuario.infrastructure.adapter.in.web.mapper;

import com.seb.msusuario.domain.model.Direccion;
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
    Direccion toDomain(DireccionRequest direccionRequest);
    DireccionResponse toResponse(Direccion direccion);
    List<DireccionResponse> toResponseList(List<Direccion> direcciones);
    List<Direccion> toDomainList(List<DireccionResponse> direccionResponses);

}
