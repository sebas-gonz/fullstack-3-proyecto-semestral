package com.seb.msinventario.domain.model;

import com.seb.msinventario.infrastructure.adapter.out.persistence.entity.InventarioEntity;
import lombok.*;

import java.time.Instant;
import java.util.UUID;
@Getter @Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Stock {
    @Setter(AccessLevel.NONE) @Builder.Default
    private UUID stockId =  UUID.randomUUID();
    private UUID productoId;
    private String lote;
    private Integer cantidad;
    private Instant fechaRegistroLote;
    private Instant fechaRegistro;
    private Instant fechaModificacion;
}
