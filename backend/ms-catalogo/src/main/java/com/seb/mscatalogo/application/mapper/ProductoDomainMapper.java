package com.seb.mscatalogo.application.mapper;

import com.seb.mscatalogo.application.port.in.command.producto.ProductoWebRequestCommand;
import com.seb.mscatalogo.domain.model.Producto;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;

import java.util.List;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface ProductoDomainMapper {
    Producto toDomain(ProductoWebRequestCommand productoWebRequestCommand);
    List<Producto> toDomainList(List<ProductoWebRequestCommand> productoWebRequestCommands);
}
