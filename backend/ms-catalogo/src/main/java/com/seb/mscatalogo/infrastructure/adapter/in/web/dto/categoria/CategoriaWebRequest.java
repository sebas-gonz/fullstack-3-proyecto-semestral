package com.seb.mscatalogo.infrastructure.adapter.in.web.dto.categoria;

import jakarta.validation.constraints.NotBlank;

public record CategoriaWebRequest(@NotBlank(message = "Escriba un nombre de categoria.") String nombre,
                                  String descripcion) {
}
