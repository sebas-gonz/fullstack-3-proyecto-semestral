package com.seb.msusuario.infrastructure.adapter.in.web.dto.direccion;

import java.math.BigDecimal;

public record DireccionResponse(String id,
                                String calle,
                                String numero,
                                String ciudad,
                                String pais,
                                BigDecimal latitude,
                                BigDecimal longitude) {
}
