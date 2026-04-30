package com.seb.mscatalogo.infrastructure.adapter.in.web.mapper;

import com.seb.mscatalogo.application.port.in.command.CategoriaProductoWebRequestCommand;
import com.seb.mscatalogo.application.port.in.command.ProductoWebRequestCommand;
import com.seb.mscatalogo.domain.model.Producto;
import com.seb.mscatalogo.infrastructure.adapter.in.web.dto.Producto.CategoriaProductoWebRequest;
import com.seb.mscatalogo.infrastructure.adapter.in.web.dto.Producto.ProductoWebRequest;
import com.seb.mscatalogo.infrastructure.adapter.in.web.dto.Producto.ProductoWebResponse;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;
import org.springframework.stereotype.Component;

import java.util.List;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING )
public interface ProductoWebMapper {
    ProductoWebRequestCommand toWebCommand(ProductoWebRequest productoWebRequest);
    ProductoWebResponse toWebResponse(Producto  producto);
    List<ProductoWebResponse> toWebResponseList(List<Producto> productos);
    List<Producto> toDomainList(List<ProductoWebRequestCommand> productoWebRequestCommands);
    CategoriaProductoWebRequestCommand toWebRequestCommand(CategoriaProductoWebRequest categoriaProductoWebRequest);
}
