package com.seb.mscatalogo.application.port.in.command;

public record ProductoWebRequestCommand(String sku, String nombre, String descripcion, Double precioBase) {
}
