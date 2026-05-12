package seb.com.msenvio.infrastructure.adapter.in.kafka.dto.pedido;

import seb.com.msenvio.infrastructure.adapter.in.kafka.dto.ubicacion.UbicacionEventDto;

import java.util.UUID;

public record PedidoPreparadoEventDto(
        UUID pedidoId,
        UbicacionEventDto origen,
        UbicacionEventDto destino
) {
}
