package com.seb.msusuario.application.service;

import com.seb.msusuario.application.exception.UserEmailExistException;
import com.seb.msusuario.application.exception.UserNotFoundException;
import com.seb.msusuario.application.mapper.UsuarioDomainMapper;
import com.seb.msusuario.application.port.in.command.CrearUsuarioCommand;
import com.seb.msusuario.domain.model.Usuario;
import com.seb.msusuario.application.port.in.UsuarioInputPort;
import com.seb.msusuario.application.port.out.UsuarioOutputPort;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class UsuarioService implements UsuarioInputPort {
    private final UsuarioOutputPort usuarioOutputPort;
    private final UsuarioDomainMapper  usuarioDomainMapper;
    @Override
    public Usuario crearUsuario(CrearUsuarioCommand  command) {
        Usuario usuario = usuarioDomainMapper.toDomain(command);
        if (usuarioOutputPort.obtenerUsuarioPorEmail(usuario.getEmail()).isPresent()) {
            throw new UserEmailExistException(usuario.getEmail());
        }
        return usuarioOutputPort.guardarUsuario(usuario);
    }

    @Override
    public void eliminarUsuario(UUID id) {
        Usuario usuario = usuarioOutputPort.obtenerUsuarioPorId(id).orElseThrow(
                () -> new UserNotFoundException(id)
        );
        usuarioOutputPort.eliminarUsuario(id);

    }

    @Override
    public Usuario obtenerUsuarioPorId(UUID id) {
        return usuarioOutputPort.obtenerUsuarioPorId(id)
                .orElseThrow(() -> new UserNotFoundException(id));
    }

    @Override
    public Usuario obtenerUsuarioPorIdAuth0(String id) {
        return usuarioOutputPort.obtenerUsuarioPorIdAuth0(id)
                .orElseThrow(() -> new UserNotFoundException(id));
    }

    @Override
    public Usuario actualizarUsuario(UUID id,CrearUsuarioCommand command) {
        Usuario existente = usuarioOutputPort.obtenerUsuarioPorId(id).orElseThrow(
                () -> new UserNotFoundException(id));
        usuarioOutputPort.obtenerUsuarioPorEmail(command.email()).ifPresent(
                usuarioConEmail -> {
                    if(!usuarioConEmail.getUsuarioId().equals(existente.getUsuarioId())){
                        throw new UserEmailExistException(existente.getEmail());
                    }
                }
            );
        existente.setEmail(command.email());
        existente.setNombre(command.nombre());
        existente.setApellido(command.apellido());
        return usuarioOutputPort.guardarUsuario(existente);
    }

    @Override
    public List<Usuario> obtenerTodosUsuarios() {
        return  usuarioOutputPort.obtenerTodosUsuarios();
    }
}
