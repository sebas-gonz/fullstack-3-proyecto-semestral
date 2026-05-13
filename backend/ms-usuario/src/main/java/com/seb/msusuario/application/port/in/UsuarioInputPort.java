package com.seb.msusuario.application.port.in;

import com.seb.msusuario.application.port.in.command.CrearUsuarioCommand;
import com.seb.msusuario.domain.model.Usuario;
import com.seb.msusuario.infrastructure.adapter.in.web.dto.usuario.UsuarioRequest;
import com.seb.msusuario.infrastructure.adapter.in.web.dto.usuario.UsuarioResponse;

import java.util.List;
import java.util.UUID;

public interface UsuarioInputPort {
    Usuario crearUsuario(CrearUsuarioCommand command);
    void eliminarUsuario(UUID id);
    Usuario obtenerUsuarioPorId(UUID id);
    Usuario obtenerUsuarioPorIdAuth0(String id);

    Usuario actualizarUsuario(UUID id, CrearUsuarioCommand command);

    List<Usuario> obtenerTodosUsuarios();
}
