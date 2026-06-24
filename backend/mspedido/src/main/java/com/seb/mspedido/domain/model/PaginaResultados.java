package com.seb.mspedido.domain.model;

import java.util.List;

public record PaginaResultados<T>(List<T> contenido, long  totalElementos, long totalPaginas) {
}
