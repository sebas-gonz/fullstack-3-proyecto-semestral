package seb.com.msenvio.infrastructure.adapter.in.web.dto.ubicacion;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;

public record UbicacionWebRequest(@NotBlank(message = "La calle esta vacia") String calle,
                                  @NotBlank(message = "El numero de la ubicacion esta vacio") String numero,
                                  @NotBlank(message = "La ciudad de la ubicacion esta vacia") String ciudad,
                                  @NotNull(message = "Latitud de la ubicacion esta vacio") BigDecimal latitude,
                                  @NotNull(message = "Longitud de la ubicacion esta vacio") BigDecimal longitude) {
}
