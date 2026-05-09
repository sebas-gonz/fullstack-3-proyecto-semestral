package com.seb.mscatalogo.infrastructure.adapter.in.kafka.mapper;

import com.seb.mscatalogo.application.port.in.command.producto.ActualizarStockProductoCommand;
import com.seb.mscatalogo.infrastructure.adapter.in.kafka.dto.ProductoStockEventDto;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface ProductoStockKafkaMapper {
    ActualizarStockProductoCommand toCommand(ProductoStockEventDto productoStockEventDto);
}
