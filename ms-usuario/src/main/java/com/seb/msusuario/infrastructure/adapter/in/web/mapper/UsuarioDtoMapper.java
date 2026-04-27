package com.seb.msusuario.infrastructure.adapter.in.web.mapper;


import com.seb.msusuario.domain.model.Usuario;
import com.seb.msusuario.infrastructure.adapter.in.web.dto.usuario.UsuarioRequest;
import com.seb.msusuario.infrastructure.adapter.in.web.dto.usuario.UsuarioResponse;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;

import java.util.List;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING, uses = DireccionDtoMapper.class)
public interface UsuarioDtoMapper {
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "fechaRegistro", ignore = true)
    @Mapping(target = "fechaModificacion", ignore = true)
    @Mapping(target = "direcciones", ignore = true)
    Usuario toDomain( UsuarioRequest usuarioRequest);
    Usuario toDomain(String id, UsuarioRequest usuarioRequest);
    UsuarioResponse toResponse(Usuario usuario);
    List<UsuarioResponse> toResponseList(List<Usuario> usuarios);
}
