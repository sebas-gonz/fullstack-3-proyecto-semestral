package com.seb.msusuario.infrastructure.adapter.in.web.dto.direccion;

import jakarta.validation.constraints.NotBlank;

public record DireccionRequest(@NotBlank(message = "Ingrese el país.") String pais,
                               @NotBlank(message = "Escriba una ciudad.") String ciudad,
                               @NotBlank(message = "Escriba la calle.") String calle,
                               @NotBlank(message = "Escriba el numero de la direccion") String numero) {
}
