package com.seb.mspedido;

import com.seb.mspedido.application.port.in.DetalleInputPort;
import com.seb.mspedido.domain.model.Detalle;
import com.seb.mspedido.infrastructure.adapter.in.web.controller.DetallePedidoController;
import com.seb.mspedido.infrastructure.adapter.in.web.dto.detalle.DetalleWebResponse;
import com.seb.mspedido.infrastructure.adapter.in.web.mapper.DetalleWebMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.kafka.autoconfigure.KafkaAutoConfiguration;
import org.springframework.boot.security.autoconfigure.SecurityAutoConfiguration;
import org.springframework.boot.security.oauth2.server.resource.autoconfigure.servlet.OAuth2ResourceServerAutoConfiguration;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.cache.CacheManager;
import org.springframework.data.jpa.mapping.JpaMetamodelMappingContext;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;
import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;

@WebMvcTest(
        controllers = DetallePedidoController.class,
        excludeAutoConfiguration = {
                SecurityAutoConfiguration.class,
                OAuth2ResourceServerAutoConfiguration.class,
                KafkaAutoConfiguration.class
        }
)
@AutoConfigureMockMvc(addFilters = false)
public class DetallePedidoControlletTest {
    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private DetalleInputPort detalleInputPort;

    @MockitoBean
    private DetalleWebMapper detalleWebMapper;

    @MockitoBean
    private JpaMetamodelMappingContext jpaMappingContext;
    @MockitoBean
    private CacheManager cacheManager;

    private final UUID PEDIDO_ID = UUID.randomUUID();
    private final UUID DETALLE_ID = UUID.randomUUID();

    @Test
    void obtenerDetallesPedido_DebeRetornarLista200Ok() throws Exception {
        when(detalleInputPort.obtenerDetallesPorPedido(PEDIDO_ID)).thenReturn(List.of(new Detalle()));
        when(detalleWebMapper.toResponseList(any())).thenReturn(List.of(mock(DetalleWebResponse.class)));

        mockMvc.perform(get("/api/v1/pedidos/{pedidoid}/detalles", PEDIDO_ID)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$.length()").value(1));
    }

    @Test
    void obtenerDetalle_DebeRetornarDetalle200Ok() throws Exception {
        when(detalleInputPort.obtenerDetalle(eq(PEDIDO_ID), eq(DETALLE_ID))).thenReturn(new Detalle());
        when(detalleWebMapper.toResponse(any())).thenReturn(mock(DetalleWebResponse.class));

        mockMvc.perform(get("/api/v1/pedidos/{pedidoid}/detalles/{id}", PEDIDO_ID, DETALLE_ID)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }
}
