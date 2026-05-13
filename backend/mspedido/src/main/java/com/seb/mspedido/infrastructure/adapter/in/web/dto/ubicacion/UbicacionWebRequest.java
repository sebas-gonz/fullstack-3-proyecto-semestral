package com.seb.mspedido.infrastructure.adapter.in.web.dto.ubicacion;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;

public record UbicacionWebRequest(@NotBlank(message = "Escriba una calle")
                                  String calle,
                                  @NotBlank(message = "Escriba el numero de la ubicacion")
                                  String numero,
                                  @NotBlank(message = "Escriba la ciudad e la ubicacion.")
                                  String ciudad,
                                  @NotNull(message = "Latitud nula")
                                  BigDecimal latitude,
                                  @NotNull(message = "Longitud nula")
                                  BigDecimal longitude
                                  ) {
}
