package com.seb.msusuario.application.service;

import com.seb.msusuario.domain.model.Usuario;
import com.seb.msusuario.application.port.in.UsuarioInputPort;
import com.seb.msusuario.application.port.out.UsuarioOutputPort;
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
    @Override
    public Usuario crearUsuario(Usuario usuario) {
        if (usuarioOutputPort.obtenerUsuarioPorEmail(usuario.getEmail()).isPresent()
            || usuarioOutputPort.obtenerUsuarioPorId(usuario.getId()).isPresent()
            || usuarioOutputPort.obtenerUsuarioPorIdAuth0(usuario.getIdAuth0()).isPresent()){
            throw new RuntimeException("El usuario ya existe en el sistema");
        }
        else {
            return (usuarioOutputPort.guardarUsuario(usuario));
        }
    }

    @Override
    public void elminiarUsuario(String id) {
        Optional<Usuario> usuarioOptional = usuarioOutputPort.obtenerUsuarioPorId(id);
        if (usuarioOptional.isEmpty()){
            throw new RuntimeException("El usuario no existe en el sistema");
        }
        usuarioOutputPort.eliminarUsuario(id);

    }

    @Override
    public Usuario obtenerUsuarioPorId(String id) {
        Optional<Usuario> usuario = usuarioOutputPort.obtenerUsuarioPorId(id);
        if (usuario.isEmpty()){
            throw new RuntimeException("El usuario no existe en el sistema");
        } else {
            return usuario.get();
        }
    }

    @Override
    public Usuario actualizarUsuario(Usuario usuario) {
        Usuario existente = usuarioOutputPort.obtenerUsuarioPorId(usuario.getId()).orElseThrow(
                () -> new RuntimeException("El usuario no existe en el sistema"));
        usuarioOutputPort.obtenerUsuarioPorEmail(usuario.getEmail()).ifPresent(
                usuarioConEmail -> {
                    if(!usuarioConEmail.getId().equals(usuario.getId())){
                        throw new RuntimeException("El correo ya existe en el sistema");
                    }
                }
            );
        existente.setEmail(usuario.getEmail());
        existente.setNombre(usuario.getNombre());
        existente.setApellido(usuario.getApellido());
        return usuarioOutputPort.actualizarUsuario(existente);
    }

    @Override
    public List<Usuario> obtenerTodosUsuarios() {
        List<Usuario> usuarios = usuarioOutputPort.obtenerTodosUsuarios();
        if (usuarios.isEmpty()){
            throw new RuntimeException("No existen usuarios en el sistema");
        } else {
            return usuarios;
        }
    }
}
