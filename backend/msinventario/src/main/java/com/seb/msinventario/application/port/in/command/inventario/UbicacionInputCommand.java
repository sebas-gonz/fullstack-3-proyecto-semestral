package com.seb.msinventario.application.port.in.command.inventario;

import java.math.BigDecimal;

public record UbicacionInputCommand(String calle,
                                    String numero,
                                    String ciudad,
                                    BigDecimal latitude,
                                    BigDecimal longitude) {
}
