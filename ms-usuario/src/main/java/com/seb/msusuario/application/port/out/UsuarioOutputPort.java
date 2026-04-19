package com.seb.msusuario.application.port.out;

import com.seb.msusuario.domain.model.Usuario;

import java.util.List;
import java.util.Optional;

public interface UsuarioOutputPort {
    Usuario guardarUsuario(Usuario usuario);
    Usuario actualizarUsuario(Usuario usuario);
    void eliminarUsuario(String id);
    Optional<Usuario> obtenerUsuarioPorId(String id);
    Optional<Usuario> obtenerUsuarioPorEmail(String email);
    Optional<Usuario> obtenerUsuarioPorIdAuth0(String idAuth0);
    List<Usuario> obtenerTodosUsuarios();
}
