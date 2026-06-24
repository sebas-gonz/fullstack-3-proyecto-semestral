package seb.com.msenvio;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import seb.com.msenvio.application.mapper.EnvioDomainMapper;
import seb.com.msenvio.application.port.in.command.envio.CrearEnvioCommand;
import seb.com.msenvio.application.port.out.EnvioEventPublisherPort;
import seb.com.msenvio.application.port.out.EnvioOutputPort;
import seb.com.msenvio.application.service.EnvioService;
import seb.com.msenvio.domain.model.Envio;
import seb.com.msenvio.domain.model.EstadoEnvio;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class EnvioServiceTest {
    @Mock
    private EnvioOutputPort envioOutputPort;
    @Mock
    private EnvioDomainMapper envioDomainMapper;
    @Mock
    private EnvioEventPublisherPort envioEventPublisherPort;

    @InjectMocks
    private EnvioService envioService;

    private Envio envioMock;
    private CrearEnvioCommand crearEnvioCommandMock;
    private Pageable pageableMock;

    private final UUID ENVIO_ID = UUID.randomUUID();
    private final UUID PEDIDO_ID = UUID.randomUUID();
    private final UUID REPARTIDOR_ID = UUID.randomUUID();

    @BeforeEach
    void setUp() {
        envioMock = Envio.builder()
                .envioId(ENVIO_ID)
                .pedidoId(PEDIDO_ID)
                .build();

        crearEnvioCommandMock = mock(CrearEnvioCommand.class);
        pageableMock = mock(Pageable.class);
    }

    // Crear envio

    @Test
    void crearEnvio_DebeSetearEstadoDisponibleYGuardar() {
        when(envioDomainMapper.toDomain(crearEnvioCommandMock)).thenReturn(envioMock);
        when(envioOutputPort.guardarEnvio(envioMock)).thenReturn(envioMock);

        Envio resultado = envioService.crearEnvio(crearEnvioCommandMock);

        assertNotNull(resultado);
        assertEquals(EstadoEnvio.DISPONIBLE, resultado.getEstado());
        verify(envioOutputPort, times(1)).guardarEnvio(envioMock);
    }

    @Test
    void asignarRepartidor_DebeAsignarYGuardar() {
        when(envioOutputPort.obtenerEnvioPorId(ENVIO_ID)).thenReturn(Optional.of(envioMock));
        when(envioOutputPort.guardarEnvio(envioMock)).thenReturn(envioMock);

        Envio resultado = envioService.asignarRepartidor(ENVIO_ID, REPARTIDOR_ID);

        assertNotNull(resultado);
        assertEquals(REPARTIDOR_ID, resultado.getRepartidorId());
        verify(envioOutputPort, times(1)).guardarEnvio(envioMock);
    }

    @Test
    void asignarRepartidor_DebeLanzarExcepcion_CuandoNoExiste() {
        when(envioOutputPort.obtenerEnvioPorId(ENVIO_ID)).thenReturn(Optional.empty());

        assertThrows(RuntimeException.class, () -> envioService.asignarRepartidor(ENVIO_ID, REPARTIDOR_ID));
    }

    // Actualizar estado de un envio

    @Test
    void actualizarEstado_AEnRuta_DebePublicarEventoEnRuta() {
        when(envioOutputPort.obtenerEnvioPorId(ENVIO_ID)).thenReturn(Optional.of(envioMock));
        when(envioOutputPort.guardarEnvio(envioMock)).thenReturn(envioMock);

        Envio resultado = envioService.actualizarEstado(ENVIO_ID, EstadoEnvio.EN_RUTA);

        assertEquals(EstadoEnvio.EN_RUTA, resultado.getEstado());
        verify(envioEventPublisherPort, times(1)).publicarEnvioEnRuta(PEDIDO_ID);
        verify(envioEventPublisherPort, never()).publicarEnvioEntregado(any());
    }

    @Test
    void actualizarEstado_AEntregado_DebePublicarEventoEntregado() {
        when(envioOutputPort.obtenerEnvioPorId(ENVIO_ID)).thenReturn(Optional.of(envioMock));
        when(envioOutputPort.guardarEnvio(envioMock)).thenReturn(envioMock);

        Envio resultado = envioService.actualizarEstado(ENVIO_ID, EstadoEnvio.ENTREGADO);

        assertEquals(EstadoEnvio.ENTREGADO, resultado.getEstado());
        verify(envioEventPublisherPort, times(1)).publicarEnvioEntregado(PEDIDO_ID);
        verify(envioEventPublisherPort, never()).publicarEnvioEnRuta(any());
    }

    @Test
    void actualizarEstado_ADisponible_NoDebePublicarEventos() {
        when(envioOutputPort.obtenerEnvioPorId(ENVIO_ID)).thenReturn(Optional.of(envioMock));
        when(envioOutputPort.guardarEnvio(envioMock)).thenReturn(envioMock);

        Envio resultado = envioService.actualizarEstado(ENVIO_ID, EstadoEnvio.DISPONIBLE);

        assertEquals(EstadoEnvio.DISPONIBLE, resultado.getEstado());
        verify(envioEventPublisherPort, never()).publicarEnvioEnRuta(any());
        verify(envioEventPublisherPort, never()).publicarEnvioEntregado(any());
    }

    @Test
    void actualizarEstado_DebeLanzarExcepcion_CuandoNoExiste() {
        when(envioOutputPort.obtenerEnvioPorId(ENVIO_ID)).thenReturn(Optional.empty());

        assertThrows(RuntimeException.class, () -> envioService.actualizarEstado(ENVIO_ID, EstadoEnvio.EN_RUTA));
    }

    // Obtener envios

    @Test
    void obtenerEnvio_DebeRetornarEnvio() {
        when(envioOutputPort.obtenerEnvioPorId(ENVIO_ID)).thenReturn(Optional.of(envioMock));
        Envio resultado = envioService.obtenerEnvio(ENVIO_ID);
        assertNotNull(resultado);
    }

    @Test
    void obtenerEnvio_DebeLanzarExcepcion_CuandoNoExiste() {
        when(envioOutputPort.obtenerEnvioPorId(ENVIO_ID)).thenReturn(Optional.empty());
        assertThrows(RuntimeException.class, () -> envioService.obtenerEnvio(ENVIO_ID));
    }

    @Test
    void obtenerEnviosDisponibles_DebeRetornarPagina() {
        when(envioOutputPort.obtenerEnviosPorEstado(EstadoEnvio.DISPONIBLE, pageableMock))
                .thenReturn(new PageImpl<>(List.of(envioMock)));
        Page<Envio> resultado = envioService.obtenerEnviosDisponibles(pageableMock);
        assertFalse(resultado.isEmpty());
    }

    @Test
    void obtenerTodosLosEnvios_DebeRetornarPagina() {
        when(envioOutputPort.obtenerEnvios(pageableMock)).thenReturn(new PageImpl<>(List.of(envioMock)));
        Page<Envio> resultado = envioService.obtenerTodosLosEnvios(pageableMock);
        assertFalse(resultado.isEmpty());
    }

    @Test
    void obtenerEnviosAsignados_DebeRetornarPagina() {
        when(envioOutputPort.obtenerEnviosPorRepartidor(REPARTIDOR_ID, pageableMock))
                .thenReturn(new PageImpl<>(List.of(envioMock)));
        Page<Envio> resultado = envioService.obtenerEnviosAsignados(REPARTIDOR_ID, pageableMock);
        assertFalse(resultado.isEmpty());
    }

    @Test
    void obtenerEnviosEnRuta_DebeRetornarPagina() {
        when(envioOutputPort.obtenerEnviosPorEstadoYRepartidor(EstadoEnvio.EN_RUTA, REPARTIDOR_ID, pageableMock))
                .thenReturn(new PageImpl<>(List.of(envioMock)));
        Page<Envio> resultado = envioService.obtenerEnviosEnRuta(REPARTIDOR_ID, pageableMock);
        assertFalse(resultado.isEmpty());
    }

    @Test
    void obtenerEnviosEntregados_DebeRetornarPagina() {
        when(envioOutputPort.obtenerEnviosPorEstadoYRepartidor(EstadoEnvio.ENTREGADO, REPARTIDOR_ID, pageableMock))
                .thenReturn(new PageImpl<>(List.of(envioMock)));
        Page<Envio> resultado = envioService.obtenerEnviosEntregados(REPARTIDOR_ID, pageableMock);
        assertFalse(resultado.isEmpty());
    }

    @Test
    void obtenerEstadosDeEnvios_DebeRetornarListaDeNombresEnum() {
        List<String> resultado = envioService.obtenerEstadosDeEnvios();
        assertFalse(resultado.isEmpty());
        assertTrue(resultado.contains("DISPONIBLE"));
        assertTrue(resultado.contains("EN_RUTA"));
    }

    //
    // Buscar un envio
    //

    @Test
    void obtenerEnviosPorEstado_DebeRetornarTodos_CuandoEstadoEsNulo() {
        when(envioOutputPort.obtenerEnvios(pageableMock)).thenReturn(new PageImpl<>(List.of(envioMock)));
        Page<Envio> resultado = envioService.obtenerEnviosPorEstado(null, pageableMock);
        assertFalse(resultado.isEmpty());
    }

    @Test
    void obtenerEnviosPorEstado_DebeRetornarTodos_CuandoEstadoEsBlanco() {
        when(envioOutputPort.obtenerEnvios(pageableMock)).thenReturn(new PageImpl<>(List.of(envioMock)));
        Page<Envio> resultado = envioService.obtenerEnviosPorEstado("   ", pageableMock);
        assertFalse(resultado.isEmpty());
    }

    @Test
    void obtenerEnviosPorEstado_DebeRetornarFiltrado_CuandoEstadoEsValido() {
        when(envioOutputPort.obtenerEnviosPorEstado(EstadoEnvio.EN_RUTA, pageableMock))
                .thenReturn(new PageImpl<>(List.of(envioMock)));

        Page<Envio> resultado = envioService.obtenerEnviosPorEstado("EN_RUTA", pageableMock);

        assertFalse(resultado.isEmpty());
        verify(envioOutputPort, times(1)).obtenerEnviosPorEstado(EstadoEnvio.EN_RUTA, pageableMock);
    }

    @Test
    void obtenerEnviosPorEstado_DebeRetornarTodos_CuandoEstadoEsInvalido() {
        when(envioOutputPort.obtenerEnvios(pageableMock)).thenReturn(new PageImpl<>(List.of(envioMock)));
        Page<Envio> resultado = envioService.obtenerEnviosPorEstado("ESTADO_INVENTADO", pageableMock);

        assertFalse(resultado.isEmpty());
        verify(envioOutputPort, times(1)).obtenerEnvios(pageableMock);
    }
}
