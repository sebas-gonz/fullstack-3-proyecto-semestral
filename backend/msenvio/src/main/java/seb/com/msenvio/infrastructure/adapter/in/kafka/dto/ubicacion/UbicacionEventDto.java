package seb.com.msenvio.infrastructure.adapter.in.kafka.dto.ubicacion;

import java.math.BigDecimal;

public record UbicacionEventDto(
        String calle,
        String numero,
        String ciudad,
        BigDecimal latitude,
        BigDecimal longitude
) {
}
