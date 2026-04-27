package com.seb.msusuario.domain.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Usuario {
    @Builder.Default
    private String id = UUID.randomUUID().toString();
    private String nombre;
    private String apellido;
    private String email;
    private String idAuth0;
    private RolUsuario rol;
    private LocalDateTime fechaRegistro;
    private LocalDateTime fechaModificacion;
    private List<Direccion> direcciones;
}
