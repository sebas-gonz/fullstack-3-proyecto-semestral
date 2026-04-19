package com.seb.msusuario.infrastructure.adapter.out.persistence.repository;

import com.seb.msusuario.infrastructure.adapter.out.persistence.entity.DireccionEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DireccionRepository extends JpaRepository<DireccionEntity, Long> {
}
