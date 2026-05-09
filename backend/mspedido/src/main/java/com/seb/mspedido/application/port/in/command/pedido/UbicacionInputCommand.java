package com.seb.mspedido.application.port.in.command.pedido;

import java.math.BigDecimal;

public record UbicacionInputCommand(String calle,
                                    String numero,
                                    String ciudad,
                                    BigDecimal latitude,
                                    BigDecimal longitude) {
}
