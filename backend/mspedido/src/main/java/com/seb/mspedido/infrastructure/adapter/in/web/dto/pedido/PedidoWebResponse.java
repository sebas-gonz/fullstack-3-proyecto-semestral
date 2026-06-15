package com.seb.mspedido.infrastructure.adapter.in.web.dto.pedido;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.seb.mspedido.domain.model.Estado;
import com.seb.mspedido.infrastructure.adapter.in.web.dto.detalle.DetalleWebResponse;
import com.seb.mspedido.infrastructure.adapter.in.web.dto.ubicacion.UbicacionWebResponse;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.List;
import java.util.UUID;

public record PedidoWebResponse(UUID pedidoId,
                                UUID usuarioId,
                                UbicacionWebResponse origen,
                                UbicacionWebResponse destino,
                                Estado estado,
                                @JsonFormat(shape = JsonFormat.Shape.STRING)
                                Instant fechaRegistro,
                                @JsonFormat(shape = JsonFormat.Shape.STRING)
                                Instant fechaEntrega,
                                List<DetalleWebResponse> detalles,
                                BigDecimal costoEnvio,
                                BigDecimal total) {
}
