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
    //private Usuario usuario. Mientras el usuario tenga su lista de direcciones, es suficiente.
    private String calle;
    private String numero;
    private String ciudad;
    private String pais;
    private BigDecimal latitud;
    private BigDecimal longitud;
    private LocalDateTime fechaRegistro;
    private LocalDateTime fechaModificacion;
}
