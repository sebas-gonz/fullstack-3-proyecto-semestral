package com.seb.msusuario.domain.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.Instant;
import java.time.LocalDateTime;
import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Ubicacion {
    @Builder.Default
    private UUID ubicacionId = UUID.randomUUID();
    private UUID usuarioId;
    private String calle;
    private String numero;
    private String ciudad;
    private String pais;
    private BigDecimal latitude;
    private BigDecimal longitude;
    private Instant fechaRegistro;
    private Instant fechaModificacion;
}
