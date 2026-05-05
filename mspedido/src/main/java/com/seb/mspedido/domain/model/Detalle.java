package com.seb.mspedido.domain.model;

import lombok.*;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.UUID;
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Detalle {
    @Setter @Builder.Default
    private UUID detalleId = UUID.randomUUID();
    private UUID productoId;
    private UUID inventarioId;
    private BigDecimal precio;
    private Integer cantidad;
    private Instant fechaRegistro;
}
