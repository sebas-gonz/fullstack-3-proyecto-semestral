package com.seb.msusuario.application.service;

import com.seb.msusuario.application.exception.UserEmailExistException;
import com.seb.msusuario.application.exception.UserNotFoundException;
import com.seb.msusuario.domain.model.Usuario;
import com.seb.msusuario.application.port.in.UsuarioInputPort;
import com.seb.msusuario.application.port.out.UsuarioOutputPort;
import com.seb.msusuario.infrastructure.adapter.in.web.dto.usuario.UsuarioRequest;
import com.seb.msusuario.infrastructure.adapter.in.web.dto.usuario.UsuarioResponse;
import com.seb.msusuario.infrastructure.adapter.in.web.mapper.UsuarioDtoMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
@Slf4j
@Service
@RequiredArgsConstructor
public class UsuarioService implements UsuarioInputPort {
    private final UsuarioOutputPort usuarioOutputPort;
    private final UsuarioDtoMapper usuarioDtoMapper;
    @Override
    public UsuarioResponse crearUsuario(UsuarioRequest usuarioRequest) {
        Usuario usuario = usuarioDtoMapper.toDomain(usuarioRequest);
        if (usuarioOutputPort.obtenerUsuarioPorEmail(usuario.getEmail()).isPresent()) {
            throw new UserEmailExistException(usuario.getEmail());
        }
        return usuarioDtoMapper.toResponse(usuarioOutputPort.guardarUsuario(usuario));
    }

    @Override
    public void eliminarUsuario(String id) {
        Optional<Usuario> usuarioOptional = usuarioOutputPort.obtenerUsuarioPorId(id);
        if (usuarioOptional.isEmpty()){
            throw new UserNotFoundException(id);
        }
        usuarioOutputPort.eliminarUsuario(id);

    }

    @Override
    public UsuarioResponse obtenerUsuarioPorId(String id) {
        Optional<Usuario> usuario = usuarioOutputPort.obtenerUsuarioPorId(id);
        if (usuario.isEmpty()) {
            throw new UserNotFoundException(id);
        }
        return usuarioDtoMapper.toResponse(usuario.get());
    }

    @Override
    public UsuarioResponse actualizarUsuario(String id,UsuarioRequest usuarioRequest) {
        Usuario existente = usuarioOutputPort.obtenerUsuarioPorId(id).orElseThrow(
                () -> new UserNotFoundException(id));
        usuarioOutputPort.obtenerUsuarioPorEmail(usuarioRequest.email()).ifPresent(
                usuarioConEmail -> {
                    if(!usuarioConEmail.getId().equals(existente.getId())){
                        throw new UserEmailExistException(existente.getEmail());
                    }
                }
            );
        existente.setEmail(usuarioRequest.email());
        existente.setNombre(usuarioRequest.nombre());
        existente.setApellido(usuarioRequest.apellido());
        return usuarioDtoMapper.toResponse(usuarioOutputPort.guardarUsuario(existente));
    }

    @Override
    public List<UsuarioResponse> obtenerTodosUsuarios() {
        List<Usuario> usuarios = usuarioOutputPort.obtenerTodosUsuarios();
        return  usuarioDtoMapper.toResponseList(usuarios);
    }
}
