package com.seb.mscatalogo.infrastructure.adapter.in.web.dto.categoria;

import com.seb.mscatalogo.infrastructure.adapter.in.web.dto.Producto.ProductoWebResponse;

import java.time.Instant;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

public record CategoriaWebResponse(UUID categoriaId,
                                   String nombre,
                                   String descripcion,
                                   List<ProductoWebResponse> productos,
                                   Instant fechaRegistro
) {
}
