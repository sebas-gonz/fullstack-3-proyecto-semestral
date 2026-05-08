package com.seb.mspedido.infrastructure.adapter.in.web.dto.pedido;

import com.seb.mspedido.infrastructure.adapter.in.web.dto.detalle.DetalleWebRequest;

import com.seb.mspedido.infrastructure.adapter.in.web.dto.ubicacion.UbicacionWebRequest;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;

import java.util.List;
import java.util.UUID;

public record PedidoWebRequest(@NotNull(message = "El id del usuario es nulo")
                               UUID usuarioId,
                               @Valid @NotNull(message = "La ubicacion de destino es nulo")
                               UbicacionWebRequest destino,
                               @Valid @NotNull(message = "Los detalles son nulos")
                               List<DetalleWebRequest> detalles
                               ) {
}
