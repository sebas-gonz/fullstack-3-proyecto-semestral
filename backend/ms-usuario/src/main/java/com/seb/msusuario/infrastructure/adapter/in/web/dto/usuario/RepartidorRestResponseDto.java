package com.seb.msusuario.infrastructure.adapter.in.web.dto.usuario;

import com.seb.msusuario.domain.model.RolUsuario;

import java.util.UUID;

public record RepartidorRestResponseDto(UUID usuarioId, String nombre, String apellido, String email, RolUsuario rol) {
}
