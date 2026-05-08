package com.seb.mspedido.domain.model;

import java.math.BigDecimal;
import java.util.UUID;

public record ProductoReferencia(UUID productoId,
                                 String nombre,
                                 BigDecimal precio) {
}
