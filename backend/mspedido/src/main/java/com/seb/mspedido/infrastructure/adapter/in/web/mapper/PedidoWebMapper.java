package com.seb.mspedido.infrastructure.adapter.in.web.mapper;

import com.seb.mspedido.application.port.in.command.pedido.PedidoInputCommand;
import com.seb.mspedido.application.port.in.command.pedido.UbicacionInputCommand;
import com.seb.mspedido.domain.model.Pedido;
import com.seb.mspedido.domain.model.Ubicacion;
import com.seb.mspedido.infrastructure.adapter.in.web.dto.pedido.PedidoWebRequest;
import com.seb.mspedido.infrastructure.adapter.in.web.dto.pedido.PedidoWebResponse;
import com.seb.mspedido.infrastructure.adapter.in.web.dto.ubicacion.UbicacionWebRequest;
import com.seb.mspedido.infrastructure.adapter.in.web.dto.ubicacion.UbicacionWebResponse;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;
import org.springframework.stereotype.Component;

import java.util.List;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING, uses = {DetalleWebMapper.class})
public interface PedidoWebMapper {
    PedidoInputCommand toCommand(PedidoWebRequest  pedidoWebRequest);
    UbicacionInputCommand toCommand(UbicacionWebRequest ubicacionWebRequest);
    UbicacionWebResponse toResponse(Ubicacion  ubicacion);
    PedidoWebResponse toResponse(Pedido pedido);
    List<PedidoWebResponse> toResponseList(List<Pedido> pedidos);
}
