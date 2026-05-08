package com.seb.mscatalogo.infrastructure.adapter.in.web.mapper;

import com.seb.mscatalogo.application.port.in.command.categoria.CategoriaWebRequestCommand;
import com.seb.mscatalogo.domain.model.Categoria;
import com.seb.mscatalogo.infrastructure.adapter.in.web.dto.categoria.CategoriaWebRequest;
import com.seb.mscatalogo.infrastructure.adapter.in.web.dto.categoria.CategoriaWebResponse;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;

import java.util.List;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING, uses = {ProductoWebMapper.class})
public interface CategoriaWebMapper {
    CategoriaWebRequestCommand toCommand(CategoriaWebRequest categoriaWebRequest);
    CategoriaWebResponse toResponse(Categoria categoria);
    List<CategoriaWebResponse> toResponseList(List<Categoria> categorias);

}
