package seb.com.msenvio.application.port.in.command.ubicacion;

import java.math.BigDecimal;

public record UbicacionCommand(
        String calle,
        String numero,
        String ciudad,
        BigDecimal latitude,
        BigDecimal longitude
) {
}
