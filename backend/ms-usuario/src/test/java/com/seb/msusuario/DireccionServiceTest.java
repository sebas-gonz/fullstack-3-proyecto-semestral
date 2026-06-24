package com.seb.msusuario;

import com.seb.msusuario.application.exception.AddressNotFoundException;
import com.seb.msusuario.application.exception.UserNotFoundException;
import com.seb.msusuario.application.port.in.command.CrearUbicacionCommand;
import com.seb.msusuario.application.port.out.DireccionOutputPort;
import com.seb.msusuario.application.port.out.GeocodingOutPutPort;
import com.seb.msusuario.application.port.out.UsuarioOutputPort;
import com.seb.msusuario.application.service.DireccionService;
import com.seb.msusuario.domain.model.Coordenadas;
import com.seb.msusuario.domain.model.Ubicacion;
import com.seb.msusuario.domain.model.Usuario;
import com.seb.msusuario.infrastructure.adapter.in.web.mapper.DireccionWebMapper;
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
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class DireccionServiceTest {
    @Mock
    private GeocodingOutPutPort geocodingOutPutPort;
    @Mock
    private UsuarioOutputPort usuarioOutputPort;
    @Mock
    private DireccionOutputPort direccionOutputPort;
    @Mock
    private DireccionWebMapper direccionWebMapper;

    @InjectMocks
    private DireccionService direccionService;

    private Usuario usuario;
    private Ubicacion ubicacionExistente;
    private CrearUbicacionCommand command;
    private Coordenadas coordenadasMock;

    private final UUID USUARIO_ID = UUID.randomUUID();
    private final UUID DIRECCION_ID = UUID.randomUUID();

    @BeforeEach
    void setUp() {
        ubicacionExistente = Ubicacion.builder()
                .ubicacionId(DIRECCION_ID)
                .calle("Calle Vieja")
                .build();
        List<Ubicacion> direcciones = new ArrayList<>();
        direcciones.add(ubicacionExistente);

        usuario = Usuario.builder()
                .usuarioId(USUARIO_ID)
                .direcciones(direcciones)
                .build();

        command = mock(CrearUbicacionCommand.class);
        lenient().when(command.calle()).thenReturn("Nueva Calle");
        lenient().when(command.numero()).thenReturn("123");
        lenient().when(command.ciudad()).thenReturn("Ciudad");
        lenient().when(command.pais()).thenReturn("Pais");
        coordenadasMock = org.mockito.Mockito.mock(Coordenadas.class);
        lenient().when(coordenadasMock.getLatitude()).thenReturn(new BigDecimal("-34.000"));
        lenient().when(coordenadasMock.getLongitude()).thenReturn(new BigDecimal("-70.000"));
    }

    // Agregar direccion
    @Test
    void agregarDireccion_DebeGuardarYRetornarUbicacion() {
        when(usuarioOutputPort.obtenerUsuarioPorId(USUARIO_ID)).thenReturn(Optional.of(usuario));
        when(geocodingOutPutPort.obtenerCoordenadas(anyString())).thenReturn(coordenadasMock);
        when(direccionOutputPort.guardarUbicacion(any(Ubicacion.class))).thenAnswer(i -> i.getArguments()[0]);

        Ubicacion resultado = direccionService.agregarDireccion(USUARIO_ID, command);

        assertNotNull(resultado);
        assertEquals("Nueva Calle", resultado.getCalle());
        assertEquals(2, usuario.getDirecciones().size());
        verify(direccionOutputPort, times(1)).guardarUbicacion(any(Ubicacion.class));
    }

    @Test
    void agregarDireccion_DebeLanzarExcepcion_CuandoUsuarioNoExiste() {
        when(usuarioOutputPort.obtenerUsuarioPorId(USUARIO_ID)).thenReturn(Optional.empty());

        assertThrows(UserNotFoundException.class, () -> direccionService.agregarDireccion(USUARIO_ID, command));
        verify(direccionOutputPort, never()).guardarUbicacion(any());
    }


    // actualizar direccion

    @Test
    void actualizarDireccion_DebeModificarDireccionExistente() {
        when(usuarioOutputPort.obtenerUsuarioPorId(USUARIO_ID)).thenReturn(Optional.of(usuario));
        when(geocodingOutPutPort.obtenerCoordenadas(anyString())).thenReturn(coordenadasMock);
        when(usuarioOutputPort.guardarUsuario(any(Usuario.class))).thenReturn(usuario);

        Ubicacion resultado = direccionService.actualizarDireccion(USUARIO_ID, DIRECCION_ID, command);

        assertNotNull(resultado);
        assertEquals("Nueva Calle", resultado.getCalle());
        verify(usuarioOutputPort, times(2)).guardarUsuario(usuario);
    }

    @Test
    void actualizarDireccion_DebeLanzarExcepcion_CuandoDireccionNoPerteneceAlUsuario() {
        when(usuarioOutputPort.obtenerUsuarioPorId(USUARIO_ID)).thenReturn(Optional.of(usuario));

        UUID idIncorrecto = UUID.randomUUID();

        assertThrows(AddressNotFoundException.class, () -> direccionService.actualizarDireccion(USUARIO_ID, idIncorrecto, command));
    }

    // eliminar direccion
    @Test
    void eliminarDireccion_DebeEliminarDireccionDeLaLista() {
        when(usuarioOutputPort.obtenerUsuarioPorId(USUARIO_ID)).thenReturn(Optional.of(usuario));

        direccionService.eliminarDireccion(USUARIO_ID, DIRECCION_ID);

        assertEquals(0, usuario.getDirecciones().size());
        verify(usuarioOutputPort, times(1)).guardarUsuario(usuario);
    }

    @Test
    void eliminarDireccion_DebeLanzarExcepcion_CuandoDireccionNoExisteEnLista() {
        when(usuarioOutputPort.obtenerUsuarioPorId(USUARIO_ID)).thenReturn(Optional.of(usuario));
        UUID idIncorrecto = UUID.randomUUID();

        assertThrows(AddressNotFoundException.class, () -> direccionService.eliminarDireccion(USUARIO_ID, idIncorrecto));
        verify(usuarioOutputPort, never()).guardarUsuario(any());
    }

    // Obtener direcciones
    @Test
    void obtenerDireccionPorId_DebeRetornarUbicacion() {
        when(direccionOutputPort.obtenerDireccionPorId(DIRECCION_ID)).thenReturn(Optional.of(ubicacionExistente));

        Ubicacion resultado = direccionService.obtenerDireccionPorId(DIRECCION_ID);

        assertNotNull(resultado);
        assertEquals(DIRECCION_ID, resultado.getUbicacionId());
    }

    @Test
    void obtenerDireccionesPorUsuario_DebeRetornarLista() {
        when(usuarioOutputPort.obtenerUsuarioPorId(USUARIO_ID)).thenReturn(Optional.of(usuario));

        List<Ubicacion> resultado = direccionService.obtenerDireccionesPorUsuario(USUARIO_ID);

        assertEquals(1, resultado.size());
    }
}
