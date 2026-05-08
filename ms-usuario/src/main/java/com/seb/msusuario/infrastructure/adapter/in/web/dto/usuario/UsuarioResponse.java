package com.seb.msusuario.infrastructure.adapter.in.web.dto.usuario;

import com.seb.msusuario.domain.model.RolUsuario;
import com.seb.msusuario.infrastructure.adapter.in.web.dto.direccion.DireccionResponse;

import java.util.List;

public record UsuarioResponse(String usuarioId, String nombre, String apellido, String email, RolUsuario rol,
                              List<DireccionResponse> ubicaciones) {
}
