package com.seb.msinventario.infrastructure.adapter.in.web.dto.ubicacion;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;

public record UbicacionWebRequest(@NotBlank(message = "Escriba una calle.")
                                  String calle,
                                  @NotBlank(message = "Escriba el numero de la calle.,")
                                  String numero,
                                  @NotBlank(message = "Escriba el nombre de la ciudad.")
                                  String ciudad,
                                  @NotNull(message = "Las coordenadas de latitud son nulas.")
                                  BigDecimal latitude,
                                  @NotNull(message = "Las coordenadas de longitud son nulas.")
                                  BigDecimal longitude
                                  ) {
}
