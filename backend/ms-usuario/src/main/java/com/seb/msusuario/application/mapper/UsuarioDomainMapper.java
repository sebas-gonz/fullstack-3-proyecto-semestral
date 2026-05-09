package com.seb.msusuario.application.mapper;

import com.seb.msusuario.application.port.in.command.CrearUsuarioCommand;
import com.seb.msusuario.domain.model.Usuario;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;

import java.util.List;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface UsuarioDomainMapper {
    Usuario toDomain(CrearUsuarioCommand command);
    List<Usuario> toDomain(List<CrearUsuarioCommand> commands);
}
