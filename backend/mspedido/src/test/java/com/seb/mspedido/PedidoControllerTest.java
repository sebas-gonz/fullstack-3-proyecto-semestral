package com.seb.mspedido;

import com.seb.mspedido.application.port.in.PedidoInputPort;
import com.seb.mspedido.application.port.in.command.pedido.PedidoInputCommand;
import com.seb.mspedido.application.port.in.query.CotizacionEnvioQuery;
import com.seb.mspedido.domain.model.Pedido;
import com.seb.mspedido.infrastructure.adapter.in.web.controller.PedidoController;
import com.seb.mspedido.infrastructure.adapter.in.web.dto.costoenvio.CostoEnvioWebResponseDto;
import com.seb.mspedido.infrastructure.adapter.in.web.dto.pedido.PedidoWebRequest;
import com.seb.mspedido.infrastructure.adapter.in.web.dto.pedido.PedidoWebResponse;
import com.seb.mspedido.infrastructure.adapter.in.web.mapper.CostoEnvioWebMapper;
import com.seb.mspedido.infrastructure.adapter.in.web.mapper.PedidoWebMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.security.autoconfigure.SecurityAutoConfiguration;
import org.springframework.boot.security.oauth2.server.resource.autoconfigure.servlet.OAuth2ResourceServerAutoConfiguration;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.cache.CacheManager;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.mapping.JpaMetamodelMappingContext;
import org.springframework.http.MediaType;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;
import java.util.UUID;

import static org.hamcrest.Matchers.containsString;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(
        controllers = PedidoController.class,
        excludeAutoConfiguration = {
                SecurityAutoConfiguration.class,
                OAuth2ResourceServerAutoConfiguration.class
        }
)
@TestPropertySource(properties = {
        "eureka.client.enabled=false",
        "spring.cloud.discovery.enabled=false"
})
@AutoConfigureMockMvc(addFilters = false)
public class PedidoControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private PedidoInputPort pedidoInputPort;

    @MockitoBean
    private PedidoWebMapper pedidoWebMapper;

    @MockitoBean
    private CostoEnvioWebMapper costoEnvioWebMapper;

    @MockitoBean
    private JpaMetamodelMappingContext jpaMappingContext;

    @MockitoBean
    private CacheManager cacheManager;

    private final UUID PEDIDO_ID = UUID.randomUUID();
    private final UUID USUARIO_ID = UUID.randomUUID();
    private final String pedidoRequestJson = """
            {
              "usuarioId": "123e4567-e89b-12d3-a456-426614174000",
              "destino": {
                "calle": "Av Siempre Viva",
                "numero": "742",
                "ciudad": "Springfield",
                "pais": "USA",
                "latitude": -34.0,
                "longitude": -70.0
              },
              "detalles": [
                {
                  "productoId": "123e4567-e89b-12d3-a456-426614174001",
                  "inventarioId": "123e4567-e89b-12d3-a456-426614174002",
                  "cantidad": 2
                }
              ]
            }
            """;

    @Test
    void listarPedidos_DebeRetornarPaginaDePedidos() throws Exception {
        Page<Pedido> paginaMock = new PageImpl<>(List.of(new Pedido()));
        when(pedidoInputPort.obtenerPedidos(any(Pageable.class))).thenReturn(paginaMock);
        when(pedidoWebMapper.toResponse(any(Pedido.class))).thenReturn(mock(PedidoWebResponse.class));

        mockMvc.perform(get("/api/v1/pedidos")
                        .param("page", "0")
                        .param("size", "10")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content").isArray());
    }

    @Test
    void obtenerPedidoPorId_DebeRetornarPedido() throws Exception {
        when(pedidoInputPort.obtenerPedido(PEDIDO_ID)).thenReturn(new Pedido());
        when(pedidoWebMapper.toResponse(any(Pedido.class))).thenReturn(mock(PedidoWebResponse.class));

        mockMvc.perform(get("/api/v1/pedidos/{id}", PEDIDO_ID)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    void crearPedido_DebeRetornar201CreatedYLocation() throws Exception {
        PedidoWebResponse mockResponse = mock(PedidoWebResponse.class);
        when(mockResponse.pedidoId()).thenReturn(PEDIDO_ID);

        when(pedidoWebMapper.toCommand(any(PedidoWebRequest.class))).thenReturn(mock(PedidoInputCommand.class));
        when(pedidoInputPort.guardarPedido(any(PedidoInputCommand.class))).thenReturn(new Pedido());
        when(pedidoWebMapper.toResponse(any(Pedido.class))).thenReturn(mockResponse);

        mockMvc.perform(post("/api/v1/pedidos")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(pedidoRequestJson))
                .andExpect(status().isCreated())
                .andExpect(header().exists("Location"))
                .andExpect(header().string("Location", containsString(PEDIDO_ID.toString())));
    }

    @Test
    void actualizarPedido_DebeRetornar200Ok() throws Exception {
        when(pedidoWebMapper.toCommand(any(PedidoWebRequest.class))).thenReturn(mock(PedidoInputCommand.class));
        when(pedidoInputPort.actualizarPedido(eq(PEDIDO_ID), any())).thenReturn(new Pedido());
        when(pedidoWebMapper.toResponse(any(Pedido.class))).thenReturn(mock(PedidoWebResponse.class));

        mockMvc.perform(put("/api/v1/pedidos/{id}", PEDIDO_ID)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(pedidoRequestJson))
                .andExpect(status().isOk());
    }

    @Test
    void eliminarPedido_DebeRetornar204NoContent() throws Exception {
        doNothing().when(pedidoInputPort).eliminarPedido(PEDIDO_ID);

        mockMvc.perform(delete("/api/v1/pedidos/{id}", PEDIDO_ID)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent());
    }

    @Test
    void obtenerPedidosPorUsuario_DebeRetornarPagina() throws Exception {
        Pedido pedidoMock = new Pedido();
        PedidoWebResponse responseMock = mock(PedidoWebResponse.class);
        Page<Pedido> paginaPedidos = new PageImpl<>(List.of(pedidoMock));
        when(pedidoInputPort.obtenerPedidosPorUsuario(eq(USUARIO_ID), any(Pageable.class)))
                .thenReturn(paginaPedidos);
        when(pedidoWebMapper.toResponse(any(Pedido.class)))
                .thenReturn(responseMock);

        mockMvc.perform(get("/api/v1/pedidos/usuario/{id}", USUARIO_ID)
                        .param("page", "0")
                        .param("size", "10")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content").isArray())
                .andExpect(jsonPath("$.content.length()").value(1))
                .andExpect(jsonPath("$.totalElements").exists());
    }

    @Test
    void cotizarCostoEnvio_DebeRetornar200Ok() throws Exception {

        String cotizacionJson = "{\"origenLatitude\":-34.0, \"origenLongitude\":-70.0, \"destinoLatitude\":-34.1, \"destinoLongitude\":-70.1}";

        when(costoEnvioWebMapper.toQuery(any())).thenReturn(mock(CotizacionEnvioQuery.class));
        when(pedidoInputPort.cotizarCostoDeEnvio(any())).thenReturn(null);
        when(costoEnvioWebMapper.toWebResponse(any())).thenReturn(mock(CostoEnvioWebResponseDto.class));

        mockMvc.perform(post("/api/v1/pedidos/cotizar-envio")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(cotizacionJson))
                .andExpect(status().isOk());
    }

    @Test
    void buscarPedido_DebeRetornarPagina() throws Exception {
        Page<Pedido> paginaMock = new PageImpl<>(List.of(new Pedido()));
        when(pedidoInputPort.buscarPedido(any(), any(Pageable.class))).thenReturn(paginaMock);
        when(pedidoWebMapper.toResponse(any(Pedido.class))).thenReturn(mock(PedidoWebResponse.class));

        mockMvc.perform(get("/api/v1/pedidos/buscar-pedido")
                        .param("parametro", "123")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content").isArray());
    }

    @Test
    void obtenerEstadosPedidos_DebeRetornarListaDeStrings() throws Exception {
        when(pedidoInputPort.obtenerEstadosPedidos()).thenReturn(List.of("PENDIENTE", "EN_RUTA"));

        mockMvc.perform(get("/api/v1/pedidos/estados")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(2))
                .andExpect(jsonPath("$[0]").value("PENDIENTE"));
    }

    @Test
    void obtenerPedidosPorEstado_DebeRetornarPagina() throws Exception {
        Page<Pedido> paginaMock = new PageImpl<>(List.of(new Pedido()));
        when(pedidoInputPort.obtenerPedidosPorEstado(eq("PENDIENTE"), any(Pageable.class))).thenReturn(paginaMock);
        when(pedidoWebMapper.toResponse(any(Pedido.class))).thenReturn(mock(PedidoWebResponse.class));

        mockMvc.perform(get("/api/v1/pedidos/estado/{estadoPedido}", "PENDIENTE")
                        .param("page", "0")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content").isArray());
    }

}
