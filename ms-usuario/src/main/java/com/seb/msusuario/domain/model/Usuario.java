package com.seb.msusuario.domain.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Usuario {
    @Builder.Default
    private UUID usuarioId = UUID.randomUUID();
    private String nombre;
    private String apellido;
    private String email;
    private String idAuth0;
    private RolUsuario rol;
    private Instant fechaRegistro;
    private Instant fechaModificacion;
    @Builder.Default
    private List<Ubicacion> direcciones = new ArrayList<>();
}
