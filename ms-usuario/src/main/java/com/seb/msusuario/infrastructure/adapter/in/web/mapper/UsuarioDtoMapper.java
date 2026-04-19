package com.seb.msusuario.infrastructure.adapter.in.web.mapper;


import com.seb.msusuario.domain.model.Usuario;
import com.seb.msusuario.infrastructure.adapter.in.web.dto.UsuarioRequest;
import com.seb.msusuario.infrastructure.adapter.in.web.dto.UsuarioResponse;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;

import java.util.List;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface UsuarioDtoMapper {
    Usuario toDomain( UsuarioRequest usuarioRequest);
    Usuario toDomain(String id, UsuarioRequest usuarioRequest);
    UsuarioResponse toResponse(Usuario usuario);
    List<UsuarioResponse> toResponseList(List<Usuario> usuarios);
}
