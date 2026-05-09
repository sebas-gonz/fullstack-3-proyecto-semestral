package com.seb.mscatalogo.application.port.in.command.producto;

import java.math.BigDecimal;

public record ProductoWebRequestCommand(String sku, String nombre, String descripcion, BigDecimal precioBase) {
}
