package com.seb.mspedido.infrastructure.adapter.in.kafka.mapper;

import com.seb.mspedido.application.port.in.command.producto.ProductoActualizadoCommand;
import com.seb.mspedido.infrastructure.adapter.in.kafka.dto.producto.ProductoPrecioEventDto;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface ProductoKafkaMapper {
    ProductoActualizadoCommand toCommand(ProductoPrecioEventDto productoPrecioEventDto);
}
