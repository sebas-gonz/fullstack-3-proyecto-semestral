package seb.com.msenvio.infrastructure.adapter.in.web.dto.ubicacion;

import java.math.BigDecimal;

public record UbicacionWebResponse(String calle,
                                   String numero,
                                   String ciudad,
                                   BigDecimal latitude,
                                   BigDecimal longitude) {
}
