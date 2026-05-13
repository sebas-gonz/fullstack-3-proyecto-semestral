package com.seb.mscatalogo.infrastructure.adapter.in.web.dto.Producto;

import jakarta.validation.constraints.NotNull;

import java.util.UUID;

public record CategoriaProductoWebRequest(@NotNull(message = "El id de la categoria esta vacio.")
                                          UUID categoriaId,
                                          @NotNull(message = "El id del producto esta vacio.")
                                          UUID productoId
                                          ) {
}
