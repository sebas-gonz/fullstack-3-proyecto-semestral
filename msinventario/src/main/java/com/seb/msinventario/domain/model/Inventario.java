package com.seb.msinventario.domain.model;

import lombok.*;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.List;
import java.util.UUID;
@Getter @Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Inventario {
    private UUID id;
    private String nombre;
    private String calle;
    private String numero;
    private String ciudad;
    private BigDecimal latitude;
    private BigDecimal longitude;
    private Instant fechaRegistro;
    private Instant fechaModificacion;
    private List<Stock> stocks;
}
