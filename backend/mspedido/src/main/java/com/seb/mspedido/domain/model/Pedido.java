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
    @Builder.Default
    private UUID pedidoId = UUID.randomUUID();
    private UUID usuarioId;
    private Ubicacion origen;
    private Ubicacion destino;
    @Builder.Default
    private Estado estado = Estado.PENDIENTE;
    private Instant fechaRegistro;
    private Instant fechaModificacion;
    private Instant fechaEntrega;
    @Builder.Default
    private List<Detalle> detalles = new ArrayList<>();
    private BigDecimal total;
    @Builder.Default
    private BigDecimal costoEnvio = BigDecimal.ZERO;
    public void calcularTotal(){
        BigDecimal subtotal = detalles.stream().map(detalle -> detalle.getPrecio().multiply(BigDecimal.valueOf(detalle.getCantidad())))
                .reduce(BigDecimal.ZERO, BigDecimal::add);
        total = subtotal.add(costoEnvio);
    }
}
