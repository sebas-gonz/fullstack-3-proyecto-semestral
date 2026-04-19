package com.seb.msusuario.application.port.in;

import com.seb.msusuario.domain.model.Usuario;

import java.util.List;

public interface UsuarioInputPort {
    Usuario crearUsuario(Usuario usuario);
    void elminiarUsuario(String id);
    Usuario obtenerUsuarioPorId(String id);

    Usuario actualizarUsuario(Usuario usuario);

    List<Usuario> obtenerTodosUsuarios();
}
