package com.seb.mspedido.application.port.in;

import com.seb.mspedido.application.port.in.command.producto.ProductoActualizadoCommand;
import com.seb.mspedido.domain.model.ProductoCache;

public interface ActualizarProductoInputPort {
    ProductoCache actualizarProducto(ProductoActualizadoCommand  productoActualizadoCommand);
}
