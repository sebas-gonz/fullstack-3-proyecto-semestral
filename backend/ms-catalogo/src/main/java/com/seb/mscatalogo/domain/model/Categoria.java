package com.seb.mscatalogo.domain.model;

import lombok.*;

import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Categoria {
    @Builder.Default
    private UUID categoriaId =  UUID.randomUUID();
    private String nombre;
    private String descripcion;
    @Builder.Default
    private List<Producto> productos = new ArrayList<>();
    private Instant fechaCreacion;
    private Instant fechaModificacion;
}
