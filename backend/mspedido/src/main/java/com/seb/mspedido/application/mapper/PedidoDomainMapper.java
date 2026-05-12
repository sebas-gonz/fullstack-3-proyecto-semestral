package com.seb.mspedido.application.mapper;

import com.seb.mspedido.application.port.in.command.pedido.PedidoInputCommand;
import com.seb.mspedido.application.port.in.command.pedido.UbicacionInputCommand;
import com.seb.mspedido.domain.model.Pedido;
import com.seb.mspedido.domain.model.Ubicacion;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface PedidoDomainMapper {
    Pedido toDomain(PedidoInputCommand pedidoInputCommand);
    Ubicacion toDomain(UbicacionInputCommand ubicacionWebRequest);
}
