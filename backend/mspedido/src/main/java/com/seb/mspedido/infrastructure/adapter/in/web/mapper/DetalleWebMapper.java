package com.seb.mspedido.infrastructure.adapter.in.web.mapper;

import com.seb.mspedido.application.port.in.command.detalle.DetalleInputCommand;
import com.seb.mspedido.domain.model.Detalle;
import com.seb.mspedido.infrastructure.adapter.in.web.dto.detalle.DetalleWebRequest;
import com.seb.mspedido.infrastructure.adapter.in.web.dto.detalle.DetalleWebResponse;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;

import java.util.List;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING )
public interface DetalleWebMapper {
    DetalleInputCommand toCommand(DetalleWebRequest detalleWebRequest);
    List<DetalleInputCommand> toCommandList(List<DetalleWebRequest> detalleWebRequests);
    List<DetalleWebResponse> toResponseList(List<Detalle> detalles);
    DetalleWebResponse toResponse(Detalle detalle);
}
