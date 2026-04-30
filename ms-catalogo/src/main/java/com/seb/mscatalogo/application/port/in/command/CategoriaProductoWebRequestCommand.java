package com.seb.mscatalogo.application.port.in.command;

import java.util.UUID;

public record CategoriaProductoWebRequestCommand(UUID categoriaId, UUID productoId) {
}
