package com.seb.msusuario.infrastructure.adapter.out.persistence;

import com.seb.msusuario.domain.model.Usuario;
import com.seb.msusuario.application.port.out.UsuarioOutputPort;
import com.seb.msusuario.infrastructure.adapter.out.persistence.entity.UsuarioEntity;
import com.seb.msusuario.infrastructure.adapter.out.persistence.mapper.UsuarioMapper;
import com.seb.msusuario.infrastructure.adapter.out.persistence.repository.UsuarioRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

@Component
@RequiredArgsConstructor
public class UsuarioPersistenceAdapter implements UsuarioOutputPort {
    private final UsuarioRepository usuarioRepository;
    private final UsuarioMapper usuarioMapper;

    @Override
    public Usuario guardarUsuario(Usuario usuario) {
        UsuarioEntity usuarioEntity = usuarioMapper.toEntity(usuario);
        return usuarioMapper.toDomain(usuarioRepository.save(usuarioEntity));
    }

    @Override
    public Usuario actualizarUsuario(Usuario usuario) {
        UsuarioEntity usuarioEntity = usuarioMapper.toEntity(usuario);
        usuarioRepository.save(usuarioEntity);

        return usuarioMapper.toDomain(usuarioEntity);
    }

    @Override
    public void eliminarUsuario(String id) {
        usuarioRepository.deleteById(id);
    }

    @Override
    public Optional<Usuario> obtenerUsuarioPorId(String id) {
        return usuarioRepository.findById(id).map(usuarioMapper::toDomain);
    }

    @Override
    public Optional<Usuario> obtenerUsuarioPorEmail(String email) {
        return usuarioRepository.findByEmail(email).map(usuarioMapper::toDomain);
    }

    @Override
    public Optional<Usuario> obtenerUsuarioPorIdAuth0(String idAuth0) {
        return usuarioRepository.findByIdAuth0(idAuth0).map(usuarioMapper::toDomain);
    }

    @Override
    public List<Usuario> obtenerTodosUsuarios() {
        return usuarioRepository.findAll().stream()
                .map(usuarioMapper::toDomain).toList();
    }
}
