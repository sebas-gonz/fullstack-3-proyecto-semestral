package com.seb.mscatalogo.infrastructure.adapter.in.web.dto.Producto;

import jakarta.validation.constraints.*;

import java.math.BigDecimal;

public record ProductoWebRequest(@NotBlank(message = "Escriba un nombre de mantenimiento de existencia.") String sku,
                                 @NotBlank(message = "Escriba un nombre de producto.") String nombre,
                                 @NotBlank(message = "Escriba una descripcion de producto") String descripcion,
                                 @NotNull(message = "Precio es obligatorio.") @Positive @Digits(integer = 10,fraction = 2)
                                 BigDecimal precioBase
                                 ) {
}
