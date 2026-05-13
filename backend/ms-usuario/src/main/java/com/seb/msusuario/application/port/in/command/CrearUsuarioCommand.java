package com.seb.msusuario.application.port.in.command;

import com.seb.msusuario.domain.model.RolUsuario;

public record CrearUsuarioCommand(String nombre,
                                  String apellido,
                                  String email,
                                  String idAuth0,
                                  RolUsuario rol) {
}
