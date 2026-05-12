package com.seb.msusuario.infrastructure.adapter.in.web.dto.usuario;

import com.seb.msusuario.domain.model.RolUsuario;
import com.seb.msusuario.infrastructure.adapter.in.web.dto.direccion.DireccionResponse;

import java.util.List;
import java.util.UUID;

public record UsuarioResponse(UUID usuarioId, String nombre, String apellido, String email, RolUsuario rol,
                              List<DireccionResponse> direcciones) {
}
