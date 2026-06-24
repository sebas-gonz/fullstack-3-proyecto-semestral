package com.seb.msusuario;

import com.seb.msusuario.application.exception.UserEmailExistException;
import com.seb.msusuario.application.exception.UserNotFoundException;
import com.seb.msusuario.application.mapper.UsuarioDomainMapper;
import com.seb.msusuario.application.port.in.command.CrearUsuarioCommand;
import com.seb.msusuario.application.port.out.UsuarioOutputPort;
import com.seb.msusuario.application.service.UsuarioService;
import com.seb.msusuario.domain.model.RolUsuario;
import com.seb.msusuario.domain.model.Usuario;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class UsuarioServiceTest {
    @Mock
    private UsuarioOutputPort usuarioOutputPort;

    @Mock
    private UsuarioDomainMapper usuarioDomainMapper;

    @InjectMocks
    private UsuarioService usuarioService;

    private Usuario usuarioPrueba;
    private CrearUsuarioCommand commandPrueba;
    private final UUID ID_PRUEBA = UUID.randomUUID();

    @BeforeEach
    void setUp() {
        usuarioPrueba = Usuario.builder()
                .usuarioId(ID_PRUEBA)
                .nombre("Seba")
                .apellido("Gonzalez")
                .email("seba@email.com")
                .rol(RolUsuario.USER)
                .build();

        commandPrueba = new CrearUsuarioCommand(
                "Seba", "Gonzalez", "seba@email.com", "auth0|123", RolUsuario.USER
        );
    }

    @Test
    void crearUsuario_Sin_Email_Registrado() {
        when(usuarioDomainMapper.toDomain(commandPrueba)).thenReturn(usuarioPrueba);
        when(usuarioOutputPort.obtenerUsuarioPorEmail(usuarioPrueba.getEmail())).thenReturn(Optional.empty());
        when(usuarioOutputPort.guardarUsuario(usuarioPrueba)).thenReturn(usuarioPrueba);

        Usuario resultado = usuarioService.crearUsuario(commandPrueba);

        assertNotNull(resultado);
        assertEquals("Seba", resultado.getNombre());
        verify(usuarioOutputPort, times(1)).guardarUsuario(usuarioPrueba);
    }

    @Test
    void crearUsuario_Con_Email_Registrado() {
        when(usuarioDomainMapper.toDomain(commandPrueba)).thenReturn(usuarioPrueba);
        when(usuarioOutputPort.obtenerUsuarioPorEmail(usuarioPrueba.getEmail())).thenReturn(Optional.of(usuarioPrueba));

        assertThrows(UserEmailExistException.class, () -> usuarioService.crearUsuario(commandPrueba));
        verify(usuarioOutputPort, never()).guardarUsuario(any());
    }
    @Test
    void eliminarUsuario() {
        when(usuarioOutputPort.obtenerUsuarioPorId(ID_PRUEBA)).thenReturn(Optional.of(usuarioPrueba));
        doNothing().when(usuarioOutputPort).eliminarUsuario(ID_PRUEBA);

        usuarioService.eliminarUsuario(ID_PRUEBA);

        verify(usuarioOutputPort, times(1)).eliminarUsuario(ID_PRUEBA);
    }

    @Test
    void eliminarUsuario_No_Existente() {
        when(usuarioOutputPort.obtenerUsuarioPorId(ID_PRUEBA)).thenReturn(Optional.empty());

        assertThrows(UserNotFoundException.class, () -> usuarioService.eliminarUsuario(ID_PRUEBA));
        verify(usuarioOutputPort, never()).eliminarUsuario(any());
    }

    @Test
    void crearUsuario_DebeGuardar_CuandoEmailNoExiste() {
        when(usuarioDomainMapper.toDomain(commandPrueba)).thenReturn(usuarioPrueba);
        when(usuarioOutputPort.obtenerUsuarioPorEmail(usuarioPrueba.getEmail())).thenReturn(Optional.empty());
        when(usuarioOutputPort.guardarUsuario(usuarioPrueba)).thenReturn(usuarioPrueba);

        Usuario resultado = usuarioService.crearUsuario(commandPrueba);

        assertNotNull(resultado);
        assertEquals("Seba", resultado.getNombre());
        verify(usuarioOutputPort, times(1)).guardarUsuario(usuarioPrueba);
    }

    @Test
    void crearUsuario_DebeLanzarExcepcion_CuandoEmailYaExiste() {
        when(usuarioDomainMapper.toDomain(commandPrueba)).thenReturn(usuarioPrueba);
        when(usuarioOutputPort.obtenerUsuarioPorEmail(usuarioPrueba.getEmail())).thenReturn(Optional.of(usuarioPrueba));

        assertThrows(UserEmailExistException.class, () -> usuarioService.crearUsuario(commandPrueba));
        verify(usuarioOutputPort, never()).guardarUsuario(any());
    }

    // TESTS PARA ELIMINAR USUARIO


    @Test
    void eliminarUsuario_DebeEliminar_CuandoUsuarioExiste() {
        when(usuarioOutputPort.obtenerUsuarioPorId(ID_PRUEBA)).thenReturn(Optional.of(usuarioPrueba));
        doNothing().when(usuarioOutputPort).eliminarUsuario(ID_PRUEBA);

        usuarioService.eliminarUsuario(ID_PRUEBA);

        verify(usuarioOutputPort, times(1)).eliminarUsuario(ID_PRUEBA);
    }

    @Test
    void eliminarUsuario_DebeLanzarExcepcion_CuandoUsuarioNoExiste() {
        when(usuarioOutputPort.obtenerUsuarioPorId(ID_PRUEBA)).thenReturn(Optional.empty());

        assertThrows(UserNotFoundException.class, () -> usuarioService.eliminarUsuario(ID_PRUEBA));
        verify(usuarioOutputPort, never()).eliminarUsuario(any());
    }

    //Test para obtener usuarios por id

    @Test
    void obtenerUsuarioPorId() {
        when(usuarioOutputPort.obtenerUsuarioPorId(ID_PRUEBA)).thenReturn(Optional.of(usuarioPrueba));

        Usuario resultado = usuarioService.obtenerUsuarioPorId(ID_PRUEBA);

        assertEquals(ID_PRUEBA, resultado.getUsuarioId());
    }

    @Test
    void obtenerUsuarioPorId_Id_No_Existe() {
        when(usuarioOutputPort.obtenerUsuarioPorId(ID_PRUEBA)).thenReturn(Optional.empty());

        assertThrows(UserNotFoundException.class, () -> usuarioService.obtenerUsuarioPorId(ID_PRUEBA));
    }

    //Test para actualizar usuario

    @Test
    void actualizarUsuario_Nuevo_Email_Sin_Uso() {
        when(usuarioOutputPort.obtenerUsuarioPorId(ID_PRUEBA)).thenReturn(Optional.of(usuarioPrueba));
        when(usuarioOutputPort.obtenerUsuarioPorEmail(commandPrueba.email())).thenReturn(Optional.empty());
        when(usuarioOutputPort.guardarUsuario(any(Usuario.class))).thenReturn(usuarioPrueba);

        Usuario resultado = usuarioService.actualizarUsuario(ID_PRUEBA, commandPrueba);

        assertNotNull(resultado);
        assertEquals(commandPrueba.nombre(), resultado.getNombre());
        verify(usuarioOutputPort, times(1)).guardarUsuario(usuarioPrueba);
    }

    @Test
    void actualizarUsuario_Con_Email_original() {
        when(usuarioOutputPort.obtenerUsuarioPorId(ID_PRUEBA)).thenReturn(Optional.of(usuarioPrueba));
        when(usuarioOutputPort.obtenerUsuarioPorEmail(commandPrueba.email())).thenReturn(Optional.of(usuarioPrueba));
        when(usuarioOutputPort.guardarUsuario(any(Usuario.class))).thenReturn(usuarioPrueba);

        Usuario resultado = usuarioService.actualizarUsuario(ID_PRUEBA, commandPrueba);

        assertNotNull(resultado);
        verify(usuarioOutputPort, times(1)).guardarUsuario(usuarioPrueba);
    }

    @Test
    void actualizarUsuario_DebeLanzarExcepcion_CuandoEmailPerteneceAOtroUsuario() {
        Usuario otroUsuario = Usuario.builder()
                .usuarioId(UUID.randomUUID())
                .email("seba@email.com")
                .build();

        when(usuarioOutputPort.obtenerUsuarioPorId(ID_PRUEBA)).thenReturn(Optional.of(usuarioPrueba));
        when(usuarioOutputPort.obtenerUsuarioPorEmail(commandPrueba.email())).thenReturn(Optional.of(otroUsuario));

        assertThrows(UserEmailExistException.class, () -> usuarioService.actualizarUsuario(ID_PRUEBA, commandPrueba));
        verify(usuarioOutputPort, never()).guardarUsuario(any());
    }

    //Paginacion de usuarios
    @Test
    void obtenerTodosUsuarios_retornaLista() {
        when(usuarioOutputPort.obtenerTodosUsuarios()).thenReturn(List.of(usuarioPrueba));

        List<Usuario> resultado = usuarioService.obtenerTodosUsuarios();

        assertFalse(resultado.isEmpty());
        assertEquals(1, resultado.size());
    }

    @Test
    void obtenerRepartidores_retornaPaginacion() {
        Page<Usuario> paginaMock = new PageImpl<>(List.of(usuarioPrueba));
        when(usuarioOutputPort.obtenerRepartidores(any(Pageable.class))).thenReturn(paginaMock);

        Page<Usuario> resultado = usuarioService.obtenerRepartidores(mock(Pageable.class));

        assertNotNull(resultado);
        assertEquals(1, resultado.getTotalElements());
    }
}
