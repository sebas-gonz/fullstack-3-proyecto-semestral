package com.seb.msbffweb;

import com.seb.msbffweb.client.EnvioClient;
import com.seb.msbffweb.client.PedidoClient;
import com.seb.msbffweb.client.UsuarioClient;
import com.seb.msbffweb.controller.EnvioBffController;
import com.seb.msbffweb.dto.out.envio.EnvioWebResponse;
import com.seb.msbffweb.dto.out.page.PaginaRestResponse;
import com.seb.msbffweb.dto.out.pedido.PedidoWebResponse;
import com.seb.msbffweb.dto.out.pedido.UbicacionWebResponse;
import com.seb.msbffweb.dto.out.usuario.UsuarioRestResponseDto;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.kafka.autoconfigure.KafkaAutoConfiguration;
import org.springframework.boot.security.autoconfigure.SecurityAutoConfiguration;
import org.springframework.boot.security.oauth2.server.resource.autoconfigure.servlet.OAuth2ResourceServerAutoConfiguration;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;
import org.springframework.core.task.AsyncTaskExecutor;
import org.springframework.core.task.SyncTaskExecutor;
import org.springframework.http.MediaType;
import org.springframework.scheduling.concurrent.ConcurrentTaskExecutor;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.List;
import java.util.UUID;

import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(
        controllers = EnvioBffController.class,
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
@Import(EnvioBffControllerTest.TestConfig.class)
public class EnvioBffControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private EnvioClient envioClient;

    @MockitoBean
    private PedidoClient pedidoClient;

    @MockitoBean
    private UsuarioClient usuarioClient;
    private final UUID ENVIO_ID = UUID.randomUUID();
    private final UUID PEDIDO_ID = UUID.randomUUID();
    private final UUID REPARTIDOR_ID = UUID.randomUUID();
    private final UUID USUARIO_ID = UUID.randomUUID();

    @TestConfiguration
    static class TestConfig {
        @Bean(name = "applicationTaskExecutor")
        public AsyncTaskExecutor applicationTaskExecutor() {
            return new ConcurrentTaskExecutor( new SyncTaskExecutor());
        }
    }

    private void prepararOrquestacionMocks(boolean conRepartidor) {

        UbicacionWebResponse ubicacionMock = new UbicacionWebResponse("Calle", "123", "Ciudad", BigDecimal.ZERO, BigDecimal.ZERO);
        EnvioWebResponse envioMock = new EnvioWebResponse(
                ENVIO_ID, PEDIDO_ID, conRepartidor ? REPARTIDOR_ID : null, "DISPONIBLE", ubicacionMock, ubicacionMock, Instant.now()
        );
        PaginaRestResponse<EnvioWebResponse> paginaMock = new PaginaRestResponse<>(List.of(envioMock), 0, 1, 1);

        PedidoWebResponse pedidoMock = new PedidoWebResponse(
                PEDIDO_ID, USUARIO_ID, ubicacionMock, ubicacionMock, "EN_PROCESO", Instant.now(), Instant.now(), List.of(), BigDecimal.ZERO, BigDecimal.TEN
        );

        UsuarioRestResponseDto compradorMock = new UsuarioRestResponseDto(USUARIO_ID, "Juan", "Perez", "juan@mail.com");
        UsuarioRestResponseDto repartidorMock = new UsuarioRestResponseDto(REPARTIDOR_ID, "Carlos", "Rider", "rider@mail.com");

        when(envioClient.obtenerTodosLosEnvios(anyInt(), anyInt())).thenReturn(paginaMock);
        when(envioClient.obtenerEnviosPorEstado(anyString(), anyInt(), anyInt())).thenReturn(paginaMock);
        when(pedidoClient.obtenerPedidoPorId(PEDIDO_ID)).thenReturn(pedidoMock);
        when(usuarioClient.obtenerUsuario(USUARIO_ID)).thenReturn(compradorMock);
        if (conRepartidor) {
            when(usuarioClient.obtenerUsuario(REPARTIDOR_ID)).thenReturn(repartidorMock);
        }
    }

    @Test
    void obtenerTodosLosEnvios_DebeRetornarPaginaEnriquecida_ConRepartidorAsignado() throws Exception {
        prepararOrquestacionMocks(true);

        mockMvc.perform(get("/api/bff/envios/admin")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content").isArray())
                .andExpect(jsonPath("$.content[0].nombreComprador").value("Juan Perez"))
                .andExpect(jsonPath("$.content[0].nombreRepartidor").value("Carlos Rider"));
    }

    @Test
    void obtenerEnviosPorEstado_DebeRetornarPaginaEnriquecida_SinRepartidor() throws Exception {
        prepararOrquestacionMocks(false);

        mockMvc.perform(get("/api/bff/envios/estado/{estadoEnvio}", "DISPONIBLE")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content").isArray())
                .andExpect(jsonPath("$.content[0].nombreComprador").value("Juan Perez"))
                .andExpect(jsonPath("$.content[0].nombreRepartidor").value("Sin asignar"));
    }

    @Test
    void obtenerEstadosPosiblesDeEnvio_DebeRetornarLista() throws Exception {
        when(envioClient.obtenerEstadosPosiblesDeEnvios()).thenReturn(List.of("DISPONIBLE", "EN_RUTA", "ENTREGADO"));

        mockMvc.perform(get("/api/bff/envios/estados-posibles")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$[0]").value("DISPONIBLE"));
    }
}
