package com.seb.mspedido;

import com.seb.mspedido.application.mapper.DetalleDomainMapper;
import com.seb.mspedido.application.port.in.command.detalle.DetalleInputCommand;
import com.seb.mspedido.application.port.out.PedidoOutputPort;
import com.seb.mspedido.application.service.DetalleService;
import com.seb.mspedido.domain.model.Detalle;
import com.seb.mspedido.domain.model.Pedido;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class DetallePedidoServiceTest {
    @Mock
    private PedidoOutputPort pedidoOutputPort;

    @Mock
    private DetalleDomainMapper detalleDomainMapper;

    @InjectMocks
    private DetalleService detalleService;

    private Pedido pedidoMock;
    private Detalle detalleMock;
    private DetalleInputCommand commandMock;

    private final UUID PEDIDO_ID = UUID.randomUUID();
    private final UUID DETALLE_ID = UUID.randomUUID();

    @BeforeEach
    void setUp() {
        detalleMock = new Detalle();
        detalleMock.setDetalleId(DETALLE_ID);

        List<Detalle> detalles = new ArrayList<>();
        detalles.add(detalleMock);

        pedidoMock = new Pedido();
        pedidoMock.setPedidoId(PEDIDO_ID);
        pedidoMock.setDetalles(detalles);
        commandMock = new DetalleInputCommand(UUID.randomUUID(), UUID.randomUUID(), 2);
    }

    // Guardar detalle
    @Test
    void guardarDetalle_ConCommand_DebeAgregarYRetornarDetalle() {
        Detalle detalleNuevo = new Detalle();
        detalleNuevo.setDetalleId(UUID.randomUUID());

        when(pedidoOutputPort.obtenerPedido(PEDIDO_ID)).thenReturn(Optional.of(pedidoMock));
        when(detalleDomainMapper.toDomain(commandMock)).thenReturn(detalleNuevo);
        when(pedidoOutputPort.guardarPedido(any(Pedido.class))).thenReturn(pedidoMock);

        Detalle resultado = detalleService.guardarDetalle(PEDIDO_ID, commandMock);

        assertNotNull(resultado);
        assertEquals(detalleNuevo.getDetalleId(), resultado.getDetalleId());
        assertEquals(2, pedidoMock.getDetalles().size());
        verify(pedidoOutputPort, times(1)).guardarPedido(pedidoMock);
    }

    @Test
    void guardarDetalle_ConCommand_DebeLanzarExcepcion_CuandoPedidoNoExiste() {
        when(pedidoOutputPort.obtenerPedido(PEDIDO_ID)).thenReturn(Optional.empty());

        assertThrows(RuntimeException.class, () -> detalleService.guardarDetalle(PEDIDO_ID, commandMock));
        verify(pedidoOutputPort, never()).guardarPedido(any());
    }

    // Guardar detalle

    @Test
    void guardarDetalle_Directo_DebeAgregarYRetornarDetalle() {
        Detalle detalleNuevo = new Detalle();
        detalleNuevo.setDetalleId(UUID.randomUUID());

        when(pedidoOutputPort.obtenerPedido(PEDIDO_ID)).thenReturn(Optional.of(pedidoMock));

        Detalle resultado = detalleService.guardarDetalle(PEDIDO_ID, detalleNuevo);

        assertNotNull(resultado);
        assertEquals(detalleNuevo.getDetalleId(), resultado.getDetalleId());
        assertEquals(2, pedidoMock.getDetalles().size());
    }

    // Obtener detalles
    @Test
    void obtenerDetalle_DebeRetornarDetalleEspecifico() {
        when(pedidoOutputPort.obtenerPedido(PEDIDO_ID)).thenReturn(Optional.of(pedidoMock));

        Detalle resultado = detalleService.obtenerDetalle(PEDIDO_ID, DETALLE_ID);

        assertNotNull(resultado);
        assertEquals(DETALLE_ID, resultado.getDetalleId());
    }

    @Test
    void obtenerDetalle_DebeLanzarExcepcion_CuandoNoEncuentraElDetalleId() {
        when(pedidoOutputPort.obtenerPedido(PEDIDO_ID)).thenReturn(Optional.of(pedidoMock));

        UUID idFalso = UUID.randomUUID();
        assertThrows(RuntimeException.class, () -> detalleService.obtenerDetalle(PEDIDO_ID, idFalso));
    }

    @Test
    void obtenerDetallesPorPedido_DebeRetornarListaDeDetalles() {
        when(pedidoOutputPort.obtenerPedido(PEDIDO_ID)).thenReturn(Optional.of(pedidoMock));

        List<Detalle> resultado = detalleService.obtenerDetallesPorPedido(PEDIDO_ID);

        assertFalse(resultado.isEmpty());
        assertEquals(1, resultado.size());
        assertEquals(DETALLE_ID, resultado.get(0).getDetalleId());
    }

    @Test
    void obtenerDetallesPorPedido_DebeLanzarExcepcion_CuandoPedidoNoExiste() {
        when(pedidoOutputPort.obtenerPedido(PEDIDO_ID)).thenReturn(Optional.empty());

        assertThrows(RuntimeException.class, () -> detalleService.obtenerDetallesPorPedido(PEDIDO_ID));
    }

    // Eliminar detalle
    @Test
    void eliminarDetalle_DebeRemoverYGuardarPedido() {
        when(pedidoOutputPort.obtenerPedido(PEDIDO_ID)).thenReturn(Optional.of(pedidoMock));

        detalleService.eliminarDetalle(PEDIDO_ID, DETALLE_ID);

        assertTrue(pedidoMock.getDetalles().isEmpty());
        verify(pedidoOutputPort, times(1)).guardarPedido(pedidoMock);
    }

    @Test
    void eliminarDetalle_DebeLanzarExcepcion_CuandoDetalleNoExisteEnElPedido() {
        when(pedidoOutputPort.obtenerPedido(PEDIDO_ID)).thenReturn(Optional.of(pedidoMock));

        UUID idFalso = UUID.randomUUID();

        RuntimeException ex = assertThrows(RuntimeException.class, () -> detalleService.eliminarDetalle(PEDIDO_ID, idFalso));
        assertEquals("Detalle no encontrado", ex.getMessage());
        verify(pedidoOutputPort, never()).guardarPedido(any());
    }
}
