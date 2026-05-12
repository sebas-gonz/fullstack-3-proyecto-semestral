package com.seb.msusuario.application.port.in.command;

public record CrearUbicacionCommand(String pais,
                                    String ciudad,
                                    String calle,
                                    String numero) {
}
