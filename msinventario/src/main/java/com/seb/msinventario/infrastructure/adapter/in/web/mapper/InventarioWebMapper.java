package com.seb.msinventario.infrastructure.adapter.in.web.mapper;

import com.seb.msinventario.application.port.in.command.inventario.InventarioInputCommand;
import com.seb.msinventario.application.port.in.command.inventario.UbicacionInputCommand;
import com.seb.msinventario.domain.model.Inventario;
import com.seb.msinventario.domain.model.Stock;
import com.seb.msinventario.domain.model.Ubicacion;
import com.seb.msinventario.infrastructure.adapter.in.web.dto.inventario.InventarioWebRequest;
import com.seb.msinventario.infrastructure.adapter.in.web.dto.inventario.InventarioWebResponse;
import com.seb.msinventario.infrastructure.adapter.in.web.dto.ubicacion.UbicacionWebRequest;
import com.seb.msinventario.infrastructure.adapter.in.web.dto.ubicacion.UbicacionWebResponse;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;

import java.util.List;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING )
public interface InventarioWebMapper {
    Inventario toDomain(InventarioInputCommand inventarioInputCommand);
    Ubicacion toDomain(UbicacionInputCommand ubicacionInputCommand);
    InventarioWebResponse toResponse(Inventario inventario);
    UbicacionWebResponse toResponse(Ubicacion ubicacion);

    List<InventarioWebResponse> toInventarioResponseList(List<Inventario> inventarios);
    List<UbicacionWebResponse> toUbicacionResponseList(List<Ubicacion> ubicaciones);
}
