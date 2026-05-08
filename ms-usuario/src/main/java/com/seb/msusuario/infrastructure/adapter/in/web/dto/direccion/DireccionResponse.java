package com.seb.msusuario.infrastructure.adapter.in.web.dto.direccion;

import java.math.BigDecimal;
import java.util.UUID;

public record DireccionResponse(UUID ubicacionId,
                                UUID usuarioId,
                                String calle,
                                String numero,
                                String ciudad,
                                String pais,
                                BigDecimal latitude,
                                BigDecimal longitude) {
}
