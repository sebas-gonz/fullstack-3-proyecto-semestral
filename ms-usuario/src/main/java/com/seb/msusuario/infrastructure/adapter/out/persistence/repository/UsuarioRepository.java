package com.seb.msusuario.infrastructure.adapter.out.persistence.repository;

import com.seb.msusuario.infrastructure.adapter.out.persistence.entity.UsuarioEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UsuarioRepository extends JpaRepository<UsuarioEntity, String> {
    Optional<UsuarioEntity> findByEmail(String email);
    Optional<UsuarioEntity> findByIdAuth0(String idAuth0);
}
