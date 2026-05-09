package com.seb.msinventario.infrastructure.adapter.in.web.dto.stock;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.time.Instant;
import java.util.UUID;

public record StockWebRequest(@NotNull(message = "El Id del producto esta vacio.")
                              UUID productoId,
                              @NotBlank(message = "El codigo del lote esta vacio")
                              String lote,
                              @NotNull(message ="La cantidad esta vacia.")
                              Integer cantidad,
                              @JsonFormat(shape = JsonFormat.Shape.STRING)
                              @NotNull(message = "La fecha del registro del lote esta vacia.")
                              Instant fechaRegistroLote
                              ) {
}
