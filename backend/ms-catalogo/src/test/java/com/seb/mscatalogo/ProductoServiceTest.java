package com.seb.mscatalogo;

import com.seb.mscatalogo.application.exception.CategoryNotFoundException;
import com.seb.mscatalogo.application.exception.ProductoNotFoundException;
import com.seb.mscatalogo.application.mapper.ProductoDomainMapper;
import com.seb.mscatalogo.application.port.in.command.categoria.CategoriaProductoWebRequestCommand;
import com.seb.mscatalogo.application.port.in.command.producto.ActualizarStockProductoCommand;
import com.seb.mscatalogo.application.port.in.command.producto.ProductoWebRequestCommand;
import com.seb.mscatalogo.application.port.out.CategoriaOutputPort;
import com.seb.mscatalogo.application.port.out.ProductoOutputPort;
import com.seb.mscatalogo.application.port.out.ProductoPublisherPort;
import com.seb.mscatalogo.application.service.ProductoService;
import com.seb.mscatalogo.domain.model.Categoria;
import com.seb.mscatalogo.domain.model.Producto;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class ProductoServiceTest {
    @Mock
    private ProductoOutputPort productoOutputPort;

    @Mock
    private CategoriaOutputPort categoriaOutputPort;

    @Mock
    private ProductoDomainMapper productoDomainMapper;

    @Mock
    private ProductoPublisherPort productoPublisherPort;

    @InjectMocks
    private ProductoService productoService;

    private UUID categoriaId;
    private UUID productoId;
    private Categoria categoria;
    private Producto producto;
    private ProductoWebRequestCommand productoWebRequestCommand;

    @BeforeEach
    void setUp() {
        categoriaId = UUID.randomUUID();
        productoId = UUID.randomUUID();

        categoria = Categoria.builder()
                .categoriaId(categoriaId)
                .nombre("Electrónica")
                .productos(new ArrayList<>())
                .build();

        producto = Producto.builder()
                .productoId(productoId)
                .categoriaId(categoriaId)
                .nombre("Laptop")
                .precioBase(new BigDecimal("1000.00"))
                .cantidadTotal(10)
                .build();

        productoWebRequestCommand = new ProductoWebRequestCommand(
                "SKU-123", "Laptop", "Laptop potente", new BigDecimal("1000.00")
        );
    }

    @Test
    void crearProducto_Exito() {

        when(categoriaOutputPort.obtenerCategoriaPorId(categoriaId)).thenReturn(Optional.of(categoria));
        when(productoDomainMapper.toDomain(productoWebRequestCommand)).thenReturn(producto);
        when(categoriaOutputPort.guardarCategoria(any(Categoria.class))).thenAnswer(invocation -> invocation.getArgument(0));

        Producto resultado = productoService.crearProducto(categoriaId, productoWebRequestCommand);

        assertNotNull(resultado);
        assertEquals(productoId, resultado.getProductoId());
        assertEquals(categoriaId, resultado.getCategoriaId());
        verify(productoPublisherPort, times(1)).publicarProductoActualizado(producto);
    }

    @Test
    void crearProducto_LanzaCategoryNotFoundException() {
        when(categoriaOutputPort.obtenerCategoriaPorId(categoriaId)).thenReturn(Optional.empty());

        assertThrows(CategoryNotFoundException.class, () ->
                productoService.crearProducto(categoriaId, productoWebRequestCommand)
        );
        verify(productoPublisherPort, never()).publicarProductoActualizado(any());
    }

    @Test
    void actualizarProducto_Exito() {
        when(productoOutputPort.obtenerProductoPorId(productoId)).thenReturn(Optional.of(producto));
        when(productoOutputPort.guardarProducto(any(Producto.class))).thenReturn(producto);

        Producto resultado = productoService.actualizarProducto(productoId, productoWebRequestCommand);

        assertNotNull(resultado);
        assertEquals("Laptop", resultado.getNombre());
        verify(productoOutputPort, times(1)).guardarProducto(producto);
        verify(productoPublisherPort, times(1)).publicarProductoActualizado(producto);
    }

    @Test
    void actualizarProducto_LanzaProductoNotFoundException() {
        when(productoOutputPort.obtenerProductoPorId(productoId)).thenReturn(Optional.empty());
        assertThrows(ProductoNotFoundException.class, () ->
                productoService.actualizarProducto(productoId, productoWebRequestCommand)
        );
    }

    @Test
    void obtenerProductoPorId_Exito() {
        when(productoOutputPort.obtenerProductoPorId(productoId)).thenReturn(Optional.of(producto));
        Producto resultado = productoService.obtenerProductoPorId(productoId);
        assertEquals(productoId, resultado.getProductoId());
    }

    @Test
    void obtenerTodosProductos_Exito() {
        when(productoOutputPort.obtenerTodosProductos()).thenReturn(List.of(producto));
        List<Producto> resultado = productoService.obtenerTodosProductos();
        assertFalse(resultado.isEmpty());
        assertEquals(1, resultado.size());
    }

    @Test
    void obtenerProductoPorCategoriaId_Exito() {
        categoria.getProductos().add(producto);
        when(categoriaOutputPort.obtenerCategoriaPorId(categoriaId)).thenReturn(Optional.of(categoria));

        List<Producto> resultado = productoService.obtenerProductoPorCategoriaId(categoriaId);

        assertFalse(resultado.isEmpty());
        assertEquals(1, resultado.size());
    }

    @Test
    void eliminarProducto_Exito() {
        when(productoOutputPort.obtenerProductoPorId(productoId)).thenReturn(Optional.of(producto));
        productoService.eliminarProducto(productoId);
        verify(productoOutputPort, times(1)).eliminarProducto(productoId);
    }

    @Test
    void actualizarCantidadProducto_Exito() {
        ActualizarStockProductoCommand command = new ActualizarStockProductoCommand(productoId, 50);
        when(productoOutputPort.obtenerProductoPorId(productoId)).thenReturn(Optional.of(producto));
        when(productoOutputPort.guardarProducto(any(Producto.class))).thenReturn(producto);

        productoService.actualizarCantidadProducto(command);

        assertEquals(50, producto.getCantidadTotal());
        verify(productoOutputPort, times(1)).guardarProducto(producto);
    }

    @Test
    void obtenerProductosDisponibles_Exito() {
        Producto productoSinStock = Producto.builder().productoId(UUID.randomUUID()).cantidadTotal(0).build();
        categoria.getProductos().add(producto);
        categoria.getProductos().add(productoSinStock);

        when(categoriaOutputPort.obtenerCategoriaPorId(categoriaId)).thenReturn(Optional.of(categoria));

        List<Producto> disponibles = productoService.obtenerProductosDisponibles(categoriaId);
        assertEquals(1, disponibles.size());
        assertEquals(productoId, disponibles.getFirst().getProductoId());
    }

    @Test
    void cambiarCategoria_Exito() {
        UUID nuevaCategoriaId = UUID.randomUUID();
        Categoria nuevaCategoria = Categoria.builder().categoriaId(nuevaCategoriaId).build();
        CategoriaProductoWebRequestCommand cmd = mock(CategoriaProductoWebRequestCommand.class);

        when(cmd.productoId()).thenReturn(productoId);
        when(cmd.categoriaId()).thenReturn(nuevaCategoriaId);

        when(productoOutputPort.obtenerProductoPorId(productoId)).thenReturn(Optional.of(producto));
        when(categoriaOutputPort.obtenerCategoriaPorId(nuevaCategoriaId)).thenReturn(Optional.of(nuevaCategoria));
        when(productoOutputPort.guardarProducto(any(Producto.class))).thenReturn(producto);

        Producto resultado = productoService.cambiarCategoria(cmd);

        assertEquals(nuevaCategoriaId, resultado.getCategoriaId());
        verify(productoOutputPort, times(1)).guardarProducto(producto);
    }

}
