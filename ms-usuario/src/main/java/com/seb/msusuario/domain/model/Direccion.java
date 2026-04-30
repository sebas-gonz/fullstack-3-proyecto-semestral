package com.seb.msusuario.domain.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Direccion {
    @Builder.Default
    private String id = UUID.randomUUID().toString();
    private String calle;
    private String numero;
    private String ciudad;
    private String pais;
    private BigDecimal latitude;
    private BigDecimal longitude;
    private LocalDateTime fechaRegistro;
    private LocalDateTime fechaModificacion;
}
