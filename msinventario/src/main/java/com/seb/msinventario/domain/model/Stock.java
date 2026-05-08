package com.seb.msinventario.domain.model;

import lombok.*;

import java.time.Instant;
import java.util.UUID;
@Getter @Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Stock {
    @Builder.Default
    private UUID stockId =  UUID.randomUUID();
    private UUID inventarioId;
    private UUID productoId;
    private String lote;
    private Integer cantidad;
    private Instant fechaRegistroLote;
    private Instant fechaRegistro;
    private Instant fechaModificacion;
}
