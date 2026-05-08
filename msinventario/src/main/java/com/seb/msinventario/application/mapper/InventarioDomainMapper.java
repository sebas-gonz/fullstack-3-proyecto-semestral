package com.seb.msinventario.application.mapper;

import com.seb.msinventario.application.port.in.command.inventario.InventarioInputCommand;
import com.seb.msinventario.application.port.in.command.inventario.UbicacionInputCommand;
import com.seb.msinventario.domain.model.Inventario;
import com.seb.msinventario.domain.model.Ubicacion;
import com.seb.msinventario.infrastructure.adapter.in.web.dto.inventario.InventarioWebRequest;
import com.seb.msinventario.infrastructure.adapter.in.web.dto.inventario.InventarioWebResponse;
import com.seb.msinventario.infrastructure.adapter.in.web.dto.ubicacion.UbicacionWebRequest;
import com.seb.msinventario.infrastructure.adapter.in.web.dto.ubicacion.UbicacionWebResponse;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface InventarioDomainMapper {
    Inventario toDomain(InventarioInputCommand inventarioInputCommand);
    Ubicacion toDomain( UbicacionInputCommand ubicacionInputCommand);
}
