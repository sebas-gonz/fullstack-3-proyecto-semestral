package com.seb.msusuario.infrastructure.adapter.out.persistence.mapper;

import com.seb.msusuario.domain.model.Direccion;
import com.seb.msusuario.infrastructure.adapter.out.persistence.entity.DireccionEntity;
import org.mapstruct.AfterMapping;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;

import java.util.List;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface DireccionMapper {
    List<DireccionEntity> toEntity(List<Direccion> direcciones);
    List<Direccion> toDomain(List<DireccionEntity> direcciones);
    DireccionEntity toEntity(Direccion direccion);
    Direccion toDomain(DireccionEntity direccionEntity);

}
