package com.seb.mscatalogo;

import com.seb.mscatalogo.application.exception.CategoryNotFoundException;
import com.seb.mscatalogo.application.port.in.command.categoria.CategoriaWebRequestCommand;
import com.seb.mscatalogo.application.port.out.CategoriaOutputPort;
import com.seb.mscatalogo.application.service.CategoriaService;
import com.seb.mscatalogo.domain.model.Categoria;
import com.seb.mscatalogo.infrastructure.adapter.in.web.mapper.CategoriaWebMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class CategoriaServiceTest {

    @Mock
    private CategoriaOutputPort categoriaOutputPort;
    @Mock
    private CategoriaWebMapper categoriaWebMapper;

    @InjectMocks
    private CategoriaService categoriaService;

    private Categoria categoriaMock;
    private CategoriaWebRequestCommand commandMock;

    private final UUID CATEGORIA_ID = UUID.randomUUID();

    @BeforeEach
    void setUp() {
        categoriaMock = Categoria.builder()
                .categoriaId(CATEGORIA_ID)
                .nombre("Tecnología")
                .descripcion("Equipos y accesorios")
                .build();

        commandMock = new CategoriaWebRequestCommand("Hogar", "Cosas para la casa");
    }

    // Crear categoria

    @Test
    void crearCategoria_DebeMapearYGuardar() {
        when(categoriaOutputPort.guardarCategoria(any(Categoria.class))).thenAnswer(invocation -> {
            return invocation.getArgument(0);
        });

        Categoria resultado = categoriaService.crearCategoria(commandMock);

        assertNotNull(resultado);
        assertEquals("Hogar", resultado.getNombre());
        assertEquals("Cosas para la casa", resultado.getDescripcion());
        verify(categoriaOutputPort, times(1)).guardarCategoria(any(Categoria.class));
    }

    // Actualizar categoria

    @Test
    void actualizarCategoria_DebeActualizarCamposYGuardar() {
        when(categoriaOutputPort.obtenerCategoriaPorId(CATEGORIA_ID)).thenReturn(Optional.of(categoriaMock));
        when(categoriaOutputPort.guardarCategoria(any(Categoria.class))).thenReturn(categoriaMock);

        Categoria resultado = categoriaService.actualizarCategoria(CATEGORIA_ID, commandMock);

        assertNotNull(resultado);
        assertEquals("Hogar", resultado.getNombre());
        assertEquals("Cosas para la casa", resultado.getDescripcion());
        verify(categoriaOutputPort, times(1)).guardarCategoria(categoriaMock);
    }

    @Test
    void actualizarCategoria_DebeLanzarExcepcion_CuandoNoExiste() {
        when(categoriaOutputPort.obtenerCategoriaPorId(CATEGORIA_ID)).thenReturn(Optional.empty());

        assertThrows(CategoryNotFoundException.class, () -> categoriaService.actualizarCategoria(CATEGORIA_ID, commandMock));
        verify(categoriaOutputPort, never()).guardarCategoria(any());
    }

    // Obtener categoria

    @Test
    void obtenerCategoria_DebeRetornarCategoria() {
        when(categoriaOutputPort.obtenerCategoriaPorId(CATEGORIA_ID)).thenReturn(Optional.of(categoriaMock));

        Categoria resultado = categoriaService.obtenerCategoria(CATEGORIA_ID);

        assertNotNull(resultado);
        assertEquals(CATEGORIA_ID, resultado.getCategoriaId());
    }

    @Test
    void obtenerCategoria_DebeLanzarExcepcion_CuandoNoExiste() {
        when(categoriaOutputPort.obtenerCategoriaPorId(CATEGORIA_ID)).thenReturn(Optional.empty());

        assertThrows(CategoryNotFoundException.class, () -> categoriaService.obtenerCategoria(CATEGORIA_ID));
    }

    @Test
    void obtenerTodosCategorias_DebeRetornarLista() {
        when(categoriaOutputPort.obtenerTodosCategorias()).thenReturn(List.of(categoriaMock));

        List<Categoria> resultado = categoriaService.obtenerTodosCategorias();

        assertFalse(resultado.isEmpty());
        assertEquals(1, resultado.size());
    }

    //Eliminae categoria

    @Test
    void eliminarCategoria_DebeEliminar_CuandoExiste() {
        when(categoriaOutputPort.obtenerCategoriaPorId(CATEGORIA_ID)).thenReturn(Optional.of(categoriaMock));
        doNothing().when(categoriaOutputPort).eliminarCategoria(CATEGORIA_ID);

        categoriaService.eliminarCategoria(CATEGORIA_ID);

        verify(categoriaOutputPort, times(1)).eliminarCategoria(CATEGORIA_ID);
    }

    @Test
    void eliminarCategoria_DebeLanzarExcepcion_CuandoNoExiste() {
        when(categoriaOutputPort.obtenerCategoriaPorId(CATEGORIA_ID)).thenReturn(Optional.empty());

        assertThrows(CategoryNotFoundException.class, () -> categoriaService.eliminarCategoria(CATEGORIA_ID));
        verify(categoriaOutputPort, never()).eliminarCategoria(any());
    }

}
