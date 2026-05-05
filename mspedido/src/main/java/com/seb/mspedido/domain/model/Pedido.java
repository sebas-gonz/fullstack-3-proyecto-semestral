package com.seb.mspedido.domain.model;

import lombok.*;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Getter @Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Pedido {
    @Setter(AccessLevel.NONE) @Builder.Default
    private UUID pedidoId = UUID.randomUUID();
    private UUID usuarioId;
    private Ubicacion ubicacionOrigen;
    private Ubicacion ubicacionDestino;
    @Builder.Default
    private Estado estado = Estado.PENDENTE;
    private Instant fechaRegistro;
    private Instant fechaModificacion;
    private Instant fechaEntrega;
    @Builder.Default
    private List<Detalle> detalles = new ArrayList<>();
    private BigDecimal total;

    public void calcularTotal(){
        total = detalles.stream().map(detalle -> detalle.getPrecio().multiply(BigDecimal.valueOf(detalle.getCantidad())))
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }
}
