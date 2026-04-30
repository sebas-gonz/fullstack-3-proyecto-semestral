package com.seb.mscatalogo.infrastructure.adapter.in.web.dto.categoria;

import com.seb.mscatalogo.infrastructure.adapter.in.web.dto.Producto.ProductoWebResponse;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

public record CategoriaWebResponse(UUID id,
                                   String nombre,
                                   String descripcion,
                                   List<ProductoWebResponse> productos,
                                   LocalDateTime fechaRegistro
) {
}
