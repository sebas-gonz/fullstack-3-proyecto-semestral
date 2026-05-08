package com.seb.mspedido.application.mapper;

import com.seb.mspedido.application.port.in.command.producto.ProductoActualizadoCommand;
import com.seb.mspedido.domain.model.ProductoCache;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface ProductoDomainMapper {
    ProductoCache toDomain(ProductoActualizadoCommand  productoActualizadoCommand);
}
