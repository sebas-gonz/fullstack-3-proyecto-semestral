package com.seb.msusuario.application.port.in;

import com.seb.msusuario.infrastructure.adapter.in.web.dto.usuario.UsuarioRequest;
import com.seb.msusuario.infrastructure.adapter.in.web.dto.usuario.UsuarioResponse;

import java.util.List;

public interface UsuarioInputPort {
    UsuarioResponse crearUsuario(UsuarioRequest usuarioRequest);
    void eliminarUsuario(String id);
    UsuarioResponse obtenerUsuarioPorId(String id);

    UsuarioResponse actualizarUsuario(String id, UsuarioRequest usuarioRequest);

    List<UsuarioResponse> obtenerTodosUsuarios();
}
