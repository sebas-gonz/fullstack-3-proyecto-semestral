package com.seb.mscatalogo.application.service;

import com.seb.mscatalogo.application.exception.CategoryNotFoundException;
import com.seb.mscatalogo.application.exception.ProductoNotFoundException;
import com.seb.mscatalogo.application.port.in.ProductoInputPort;
import com.seb.mscatalogo.application.port.in.command.CategoriaProductoWebRequestCommand;
import com.seb.mscatalogo.application.port.in.command.ProductoWebRequestCommand;
import com.seb.mscatalogo.application.port.out.CategoriaOutputPort;
import com.seb.mscatalogo.application.port.out.ProductoOutputPort;
import com.seb.mscatalogo.domain.model.Categoria;
import com.seb.mscatalogo.domain.model.Producto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class ProductoService implements ProductoInputPort {
    private final ProductoOutputPort productoOutputPort;
    private final CategoriaOutputPort categoriaOutputPort;
    @Override
    public Producto crearProducto(UUID categoriaId, ProductoWebRequestCommand  productoWebRequestCommand) {
        Categoria categoria = categoriaOutputPort.obtenerCategoriaPorId(categoriaId)
                .orElseThrow(() -> new CategoryNotFoundException(categoriaId));
        Producto nuevo = Producto.builder().sku(productoWebRequestCommand.sku())
                        .nombre(productoWebRequestCommand.nombre())
                        .descripcion(productoWebRequestCommand.descripcion())
                        .precioBase(productoWebRequestCommand.precioBase())
                        .categoria(categoria).build();
        Producto guardado = productoOutputPort.guardarProducto(nuevo);
        categoria.getProductos().add(guardado);
        return guardado;
    }

    @Override
    public Producto actualizarProducto(UUID productoId, ProductoWebRequestCommand productoWebRequestCommand) {
        Producto producto = productoOutputPort.obtenerProductoPorId(productoId).orElseThrow(
                () -> new ProductoNotFoundException(productoId)
        );
        producto.setNombre(productoWebRequestCommand.nombre());
        producto.setDescripcion(productoWebRequestCommand.descripcion());
        producto.setPrecioBase(productoWebRequestCommand.precioBase());
        return productoOutputPort.guardarProducto(producto);
    }

    @Override
    public Producto obtenerProductoPorId(UUID productoId) {
        return productoOutputPort.obtenerProductoPorId(productoId).orElseThrow(()
        -> new ProductoNotFoundException(productoId));
    }

    @Override
    public List<Producto> obtenerTodosProductos() {
        return productoOutputPort.obtenerTodosProductos();
    }

    @Override
    public List<Producto> obtenerProductoPorCategoriaId(UUID categoriaId) {
        Categoria categoria = categoriaOutputPort.obtenerCategoriaPorId(categoriaId)
                .orElseThrow(() -> new CategoryNotFoundException(categoriaId));
        return categoria.getProductos();
    }

    @Override
    public void eliminarProducto(UUID productoId) {
        Producto producto = productoOutputPort.obtenerProductoPorId(productoId).orElseThrow(
                () -> new ProductoNotFoundException(productoId)
        );
        productoOutputPort.eliminarProducto(productoId);
    }

    @Override
    public Producto cambiarCategoria(CategoriaProductoWebRequestCommand categoriaProductoWebRequestCommand) {
        Producto producto = productoOutputPort.obtenerProductoPorId(categoriaProductoWebRequestCommand.productoId())
                .orElseThrow(() -> new ProductoNotFoundException(categoriaProductoWebRequestCommand.productoId()));
        Categoria categoria = categoriaOutputPort.obtenerCategoriaPorId(categoriaProductoWebRequestCommand.categoriaId())
                .orElseThrow(() -> new CategoryNotFoundException(categoriaProductoWebRequestCommand.categoriaId()));
        producto.setCategoria(categoria);
        return productoOutputPort.guardarProducto(producto);
    }
}
