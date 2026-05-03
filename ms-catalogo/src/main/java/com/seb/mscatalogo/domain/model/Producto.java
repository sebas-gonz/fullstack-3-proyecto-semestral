package com.seb.mscatalogo.domain.model;

import lombok.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;

@Setter
@Getter
public class Producto {
    @Setter(AccessLevel.NONE)
    private UUID id =  UUID.randomUUID();
    private String sku;
    private String nombre;
    private String descripcion;
    private Double precioBase;
    private Integer cantidadTotal;
    private Categoria categoria;
    private LocalDateTime fechaRegistro;
    private LocalDateTime fechaModificacion;
    @Builder
    public  Producto(String sku,String nombre, String descripcion, Double precioBase, Categoria categoria) {
        this.id= UUID.randomUUID();
        this.sku = sku;
        this.nombre = nombre;
        this.descripcion = descripcion;
        this.precioBase = precioBase;
        this.categoria = categoria;
        this.fechaRegistro = LocalDateTime.now();
        this.fechaModificacion = LocalDateTime.now();
    }

}
