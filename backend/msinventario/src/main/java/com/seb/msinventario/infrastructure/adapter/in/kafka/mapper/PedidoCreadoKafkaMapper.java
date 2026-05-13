package com.seb.msinventario.infrastructure.adapter.in.kafka.mapper;

import com.seb.msinventario.application.port.in.command.producto.ProductoPedidoCommand;
import com.seb.msinventario.application.port.in.command.stock.DescontarStockCommand;
import com.seb.msinventario.infrastructure.adapter.in.kafka.dto.pedido.PedidoCreadoEventDto;
import com.seb.msinventario.infrastructure.adapter.in.kafka.dto.pedido.producto.ProductoPedidoEventDto;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;

import java.util.List;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface PedidoCreadoKafkaMapper {
    DescontarStockCommand toCommand(PedidoCreadoEventDto pedidoCreadoEventDto);
    ProductoPedidoCommand toCommand(ProductoPedidoEventDto productoPedidoEventDto);
    List<ProductoPedidoCommand> toCommand(List<ProductoPedidoEventDto> pedidoCreadoEventDtos);
}
