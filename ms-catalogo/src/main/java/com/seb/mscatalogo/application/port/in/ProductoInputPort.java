package com.seb.mscatalogo.application.port.in;

import com.seb.mscatalogo.application.port.in.command.CategoriaProductoWebRequestCommand;
import com.seb.mscatalogo.application.port.in.command.ProductoWebRequestCommand;
import com.seb.mscatalogo.domain.model.Producto;

import java.util.List;
import java.util.UUID;

public interface ProductoInputPort {
    Producto crearProducto(UUID categoriaId, ProductoWebRequestCommand productoWebRequestCommand);
    Producto actualizarProducto(UUID productoId, ProductoWebRequestCommand productoWebRequestCommand);
    Producto obtenerProductoPorId(UUID productoId);
    List<Producto> obtenerTodosProductos();
    List<Producto> obtenerProductoPorCategoriaId(UUID categoriaId);
    void eliminarProducto(UUID productoId);
    Producto cambiarCategoria(CategoriaProductoWebRequestCommand categoriaProductoWebRequestCommand);

}
