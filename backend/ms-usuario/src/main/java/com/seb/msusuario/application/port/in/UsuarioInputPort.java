package com.seb.msusuario.application.port.in;

import com.seb.msusuario.application.port.in.command.CrearUsuarioCommand;
import com.seb.msusuario.domain.model.Usuario;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Set;
import java.util.UUID;

public interface UsuarioInputPort {
    Usuario crearUsuario(CrearUsuarioCommand command);
    void eliminarUsuario(UUID id);
    Usuario obtenerUsuarioPorId(UUID id);
    Usuario obtenerUsuarioPorIdAuth0(String id);

    Usuario actualizarUsuario(UUID id, CrearUsuarioCommand command);

    List<Usuario> obtenerTodosUsuarios();
    Page<Usuario> obtenerRepartidores(Pageable pageable);
    List<Usuario> obtenerUsuariosPorIds(Set<UUID> usuarioIds);
}
