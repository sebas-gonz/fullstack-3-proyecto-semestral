package com.seb.msusuario.application.port.in.command;

import com.seb.msusuario.domain.model.RolUsuario;
import jakarta.validation.constraints.Email;
import org.apache.kafka.common.protocol.types.Field;

public record CrearUsuarioCommand(String nombre,
                                  String apellido,
                                  String email,
                                  String idAuth0,
                                  RolUsuario rolUsuario) {
}
