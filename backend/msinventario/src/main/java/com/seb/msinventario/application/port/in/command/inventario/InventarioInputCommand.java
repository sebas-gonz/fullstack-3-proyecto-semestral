package com.seb.msinventario.application.port.in.command.inventario;

public record InventarioInputCommand(String nombre,
                                     UbicacionInputCommand ubicacion) {
}
