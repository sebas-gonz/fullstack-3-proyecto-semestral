package com.seb.msinventario.infrastructure.adapter.in.web.dto.inventario;

import com.seb.msinventario.infrastructure.adapter.in.web.dto.ubicacion.UbicacionWebRequest;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record InventarioWebRequest(@NotBlank(message = "El nombre esta vacio.")
                                   String nombre,
                                   @NotNull @Valid
                                   UbicacionWebRequest ubicacion
                                   ) {
}
