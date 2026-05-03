package com.seb.msinventario.domain.model;

import lombok.*;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
@Getter @Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Inventario {
    @Setter(AccessLevel.NONE) @Builder.Default
    private UUID inventarioId =  UUID.randomUUID();
    private String nombre;
    private Ubicacion ubicacion;
    private Instant fechaRegistro;
    private Instant fechaModificacion;
    @Builder.Default
    private List<Stock> stocks = new ArrayList<>();
}
