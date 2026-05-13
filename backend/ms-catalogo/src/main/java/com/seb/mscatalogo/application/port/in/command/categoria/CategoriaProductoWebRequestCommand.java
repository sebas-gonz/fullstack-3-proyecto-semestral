package com.seb.mscatalogo.application.port.in.command.categoria;

import java.util.UUID;

public record CategoriaProductoWebRequestCommand(UUID categoriaId, UUID productoId) {
}
