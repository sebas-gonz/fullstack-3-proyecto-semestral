package com.seb.msinventario.infrastructure.adapter.in.web.dto.inventario;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.seb.msinventario.infrastructure.adapter.in.web.dto.stock.StockWebResponse;
import com.seb.msinventario.infrastructure.adapter.in.web.dto.ubicacion.UbicacionWebResponse;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

public record InventarioWebResponse(UUID inventarioId,
                                    String nombre,
                                    UbicacionWebResponse ubicacion,
                                    @JsonFormat(shape = JsonFormat.Shape.STRING)
                                    Instant fechaRegistro,
                                    List<StockWebResponse> stocks
                                    ) {
}
