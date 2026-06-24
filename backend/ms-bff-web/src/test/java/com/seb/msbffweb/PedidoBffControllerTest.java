package com.seb.msbffweb;

import com.seb.msbffweb.client.PedidoClient;
import com.seb.msbffweb.controller.PedidoBffController;
import com.seb.msbffweb.dto.in.pedido.CotizacionEnvioRequestDto;
import com.seb.msbffweb.dto.out.page.PaginaRestResponse;
import com.seb.msbffweb.dto.out.pedido.CostoEnvioResponseDto;
import com.seb.msbffweb.dto.out.pedido.PedidoWebResponse;
import com.seb.msbffweb.dto.out.pedido.UbicacionWebResponse;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.kafka.autoconfigure.KafkaAutoConfiguration;
import org.springframework.boot.security.autoconfigure.SecurityAutoConfiguration;
import org.springframework.boot.security.oauth2.server.resource.autoconfigure.servlet.OAuth2ResourceServerAutoConfiguration;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.List;
import java.util.UUID;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(
        controllers = PedidoBffController.class,
        excludeAutoConfiguration = {
                SecurityAutoConfiguration.class,
                OAuth2ResourceServerAutoConfiguration.class,
                KafkaAutoConfiguration.class
        }
)
@AutoConfigureMockMvc(addFilters = false)
@TestPropertySource(properties = {
        "eureka.client.enabled=false",
        "spring.cloud.discovery.enabled=false"
})
public class PedidoBffControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private PedidoClient pedidoClient;

    private final UUID PEDIDO_ID = UUID.randomUUID();
    private final UUID USUARIO_ID = UUID.randomUUID();
    private final String cotizacionRequestJson = """
            {
              "origenLatitude": -34.1708,
              "origenLongitude": -70.7444,
              "destinoLatitude": -34.1800,
              "destinoLongitude": -70.7500
            }
            """;

    @Test
    void cotizarEnvio_DebeRetornarCosto_200Ok() throws Exception {
        CostoEnvioResponseDto responseDto = new CostoEnvioResponseDto(new BigDecimal("5000.00"), 5.0);

        when(pedidoClient.cotizarEnvio(any(CotizacionEnvioRequestDto.class))).thenReturn(responseDto);

        mockMvc.perform(post("/api/bff/pedidos/cotizar-envio")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(cotizacionRequestJson))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.costoEnvio").value(5000.00));
    }

    @Test
    void buscarPedido_ConParametro_DebeRetornarPagina_200Ok() throws Exception {
        UbicacionWebResponse ubicacionMock = new UbicacionWebResponse("Calle", "1", "Ciudad", BigDecimal.ZERO, BigDecimal.ZERO);
        PedidoWebResponse pedidoMock = new PedidoWebResponse(
                PEDIDO_ID, USUARIO_ID, ubicacionMock, ubicacionMock, "EN_PREPARACION", Instant.now(), Instant.now(), List.of(), BigDecimal.ZERO, BigDecimal.TEN
        );
        PaginaRestResponse<PedidoWebResponse> paginaMock = new PaginaRestResponse<>(List.of(pedidoMock), 0, 1, 1);

        when(pedidoClient.buscarPedido(eq("PED-123"), anyInt(), anyInt())).thenReturn(paginaMock);

        mockMvc.perform(get("/api/bff/pedidos/buscar-pedido")
                        .param("parametro", "PED-123")
                        .param("page", "0")
                        .param("size", "10")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content").isArray())
                .andExpect(jsonPath("$.content[0].pedidoId").value(PEDIDO_ID.toString()));
    }

    @Test
    void buscarPedido_SinParametro_DebeRetornarPagina_200Ok() throws Exception {
        PaginaRestResponse<PedidoWebResponse> paginaMock = new PaginaRestResponse<>(List.of(), 0, 0, 0);

        when(pedidoClient.buscarPedido(eq(null), anyInt(), anyInt())).thenReturn(paginaMock);

        mockMvc.perform(get("/api/bff/pedidos/buscar-pedido")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content").isArray())
                .andExpect(jsonPath("$.content").isEmpty());
    }

    @Test
    void estadosPedidos_DebeRetornarLista_200Ok() throws Exception {
        when(pedidoClient.obtenerEstadosPedidos()).thenReturn(List.of("EN_PREPARACION", "EN_RUTA", "ENTREGADO"));

        mockMvc.perform(get("/api/bff/pedidos/estados-pedidos")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$[0]").value("EN_PREPARACION"));
    }

    @Test
    void buscarPedidosPorEstado_DebeRetornarPagina_200Ok() throws Exception {
        PaginaRestResponse<PedidoWebResponse> paginaMock = new PaginaRestResponse<>(List.of(), 0, 0, 0);

        when(pedidoClient.obtenerPedidosPorEstado(eq("EN_PREPARACION"), anyInt(), anyInt())).thenReturn(paginaMock);

        mockMvc.perform(get("/api/bff/pedidos/estado/{estadoPedido}", "EN_PREPARACION")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content").isArray());
    }
}
