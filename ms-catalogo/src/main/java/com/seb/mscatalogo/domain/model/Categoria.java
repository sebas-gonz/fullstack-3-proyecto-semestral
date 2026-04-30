package com.seb.mscatalogo.domain.model;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
@Setter
@Getter
public class Categoria {
    @Setter(AccessLevel.NONE)
    private UUID id;
    private String nombre;
    private String descripcion;
    private List<Producto> productos;
    private LocalDateTime fechaCreacion;
    private LocalDateTime fechaModificacion;
    @Builder
    public Categoria(String nombre, String descripcion, List<Producto> productos) {
        this.id = UUID.randomUUID();
        this.nombre = nombre;
        this.descripcion = descripcion;
        this.productos = productos == null ? new ArrayList<Producto>() : productos;
        this.fechaCreacion = LocalDateTime.now();
        this.fechaModificacion = LocalDateTime.now();
    }
}
