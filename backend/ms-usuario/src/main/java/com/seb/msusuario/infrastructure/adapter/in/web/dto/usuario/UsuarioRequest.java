package com.seb.msusuario.infrastructure.adapter.in.web.dto.usuario;

import com.seb.msusuario.domain.model.RolUsuario;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Null;

public record UsuarioRequest(@NotBlank(message = "Escriba un nombre.") String nombre,
                             @NotBlank(message = "Escriba un apellido.") String apellido,
                             @Email(message = "Escriba un correo valido.") @NotBlank(message = "Escriba un correo.")
                             String email,
                             String idAuth0,
                             @NotNull(message = "El usuario debe tener un rol definido.")
                             RolUsuario rol) {

}
