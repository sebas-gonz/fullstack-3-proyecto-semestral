package com.seb.msinventario.infrastructure.adapter.in.kafka.dto.pedido.producto;

import java.util.UUID;

public record ProductoPedidoEventDto(UUID inventarioId,
                                     UUID productoId,
                                     Integer cantidad) {

}
