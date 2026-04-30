package com.seb.mscatalogo.infrastructure.adapter.in.web.dto.Producto;

import java.time.LocalDateTime;
import java.util.UUID;

public record ProductoWebResponse(UUID id,
                                  String sku,
                                  String nombre,
                                  String descripcion,
                                  Double precioBase,
                                  LocalDateTime fechaRegistro
                                  ) {
}
