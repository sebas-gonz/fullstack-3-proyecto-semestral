package com.seb.mspedido.domain.model;

import lombok.*;

import java.util.UUID;

@Getter @Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Pedido {
    private UUID pedidoId;
    private UUID usuarioId;
    private Ubicacion ubicacion;
    private
}
