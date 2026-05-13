package com.seb.mscatalogo.domain.model;

import lombok.*;

import java.math.BigDecimal;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;

@Setter
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Producto {
    @Builder.Default
    private UUID productoId =  UUID.randomUUID();
    private UUID categoriaId;
    private String sku;
    private String nombre;
    private String descripcion;
    private BigDecimal precioBase;
    @Builder.Default
    private Integer cantidadTotal = 0;
    private Instant fechaRegistro;
    private Instant fechaModificacion;

}
