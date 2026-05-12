package com.seb.mscatalogo.application.service;

import com.seb.mscatalogo.application.exception.CategoryNotFoundException;
import com.seb.mscatalogo.application.exception.ProductoNotFoundException;
import com.seb.mscatalogo.application.mapper.ProductoDomainMapper;
import com.seb.mscatalogo.application.port.in.ProductoInputPort;
import com.seb.mscatalogo.application.port.in.command.categoria.CategoriaProductoWebRequestCommand;
import com.seb.mscatalogo.application.port.in.command.producto.ActualizarStockProductoCommand;
import com.seb.mscatalogo.application.port.in.command.producto.ProductoWebRequestCommand;
import com.seb.mscatalogo.application.port.out.CategoriaOutputPort;
import com.seb.mscatalogo.application.port.out.ProductoOutputPort;
import com.seb.mscatalogo.application.port.out.ProductoPublisherPort;
import com.seb.mscatalogo.domain.model.Categoria;
import com.seb.mscatalogo.domain.model.Producto;
import jakarta.transaction.Transactional;
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
    private final ProductoDomainMapper productoDomainMapper;
    private final ProductoPublisherPort  productoPublisherPort;
    @Override
    @Transactional
    public Producto crearProducto(UUID categoriaId, ProductoWebRequestCommand  productoWebRequestCommand) {
        Categoria categoria = categoriaOutputPort.obtenerCategoriaPorId(categoriaId)
                .orElseThrow(() -> new CategoryNotFoundException(categoriaId));
        Producto producto = productoDomainMapper.toDomain(productoWebRequestCommand);
        producto.setCategoriaId(categoria.getCategoriaId());
        categoria.getProductos().add(producto);
        productoPublisherPort.publicarProductoActualizado(producto);
        return categoriaOutputPort.guardarCategoria(categoria).getProductos().stream()
                .filter(p -> p.getProductoId().equals(producto.getProductoId())).findFirst()
                .orElseThrow(
                        () -> new ProductoNotFoundException(producto.getProductoId())
                );
    }

    @Override
    @Transactional
    public Producto actualizarProducto(UUID productoId, ProductoWebRequestCommand productoWebRequestCommand) {
        Producto producto = productoOutputPort.obtenerProductoPorId(productoId).orElseThrow(
                () -> new ProductoNotFoundException(productoId)
        );
        producto.setNombre(productoWebRequestCommand.nombre());
        producto.setDescripcion(productoWebRequestCommand.descripcion());
        producto.setPrecioBase(productoWebRequestCommand.precioBase());
        Producto guardado = productoOutputPort.guardarProducto(producto);
        productoPublisherPort.publicarProductoActualizado(guardado);
        return guardado;
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
        producto.setCategoriaId(categoria.getCategoriaId());
        return productoOutputPort.guardarProducto(producto);
    }

    @Override
    public void actualizarCantidadProducto(ActualizarStockProductoCommand ActualizarStockProductoCommand) {
        Producto producto = productoOutputPort.obtenerProductoPorId(ActualizarStockProductoCommand.productoId())
                .orElseThrow(() -> new ProductoNotFoundException(ActualizarStockProductoCommand.productoId()));
        producto.setCantidadTotal(ActualizarStockProductoCommand.cantidadTotal());
        productoOutputPort.guardarProducto(producto);
    }

    @Override
    public List<Producto> obtenerProductosDisponibles(UUID categoriaId) {
        Categoria categoria = categoriaOutputPort.obtenerCategoriaPorId(categoriaId)
                .orElseThrow(() -> new CategoryNotFoundException(categoriaId));

        return categoria.getProductos().stream()
                .filter(producto -> producto.getCantidadTotal() > 0).toList();
    }
}
