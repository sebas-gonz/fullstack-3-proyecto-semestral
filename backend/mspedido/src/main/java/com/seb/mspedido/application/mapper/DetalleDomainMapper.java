package com.seb.mspedido.application.mapper;

import com.seb.mspedido.application.port.in.command.detalle.DetalleInputCommand;
import com.seb.mspedido.domain.model.Detalle;
import com.seb.mspedido.infrastructure.adapter.in.web.dto.detalle.DetalleWebRequest;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;

import java.util.List;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface DetalleDomainMapper {
    Detalle toDomain(DetalleInputCommand detalleInputCommand);
    List<Detalle> toDomainList(List<DetalleInputCommand> detalleInputCommands);
}
