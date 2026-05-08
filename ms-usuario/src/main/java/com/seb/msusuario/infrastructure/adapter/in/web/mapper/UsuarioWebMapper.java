package com.seb.msusuario.infrastructure.adapter.in.web.mapper;


import com.seb.msusuario.application.port.in.command.CrearUsuarioCommand;
import com.seb.msusuario.domain.model.Usuario;
import com.seb.msusuario.infrastructure.adapter.in.web.dto.usuario.UsuarioRequest;
import com.seb.msusuario.infrastructure.adapter.in.web.dto.usuario.UsuarioResponse;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;

import java.util.List;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING, uses = DireccionWebMapper.class)
public interface UsuarioWebMapper {
    CrearUsuarioCommand toCommand(UsuarioRequest usuarioRequest);
    UsuarioResponse toResponse(Usuario usuario);
    List<UsuarioResponse> toResponseList(List<Usuario> usuarios);
}
