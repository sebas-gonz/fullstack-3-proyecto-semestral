package seb.com.msenvio;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.kafka.autoconfigure.KafkaAutoConfiguration;
import org.springframework.boot.security.autoconfigure.SecurityAutoConfiguration;
import org.springframework.boot.security.oauth2.server.resource.autoconfigure.servlet.OAuth2ResourceServerAutoConfiguration;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.cache.CacheManager;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.jpa.mapping.JpaMetamodelMappingContext;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import seb.com.msenvio.application.port.in.EnvioInputPort;
import seb.com.msenvio.domain.model.Envio;
import seb.com.msenvio.domain.model.EstadoEnvio;
import seb.com.msenvio.infrastructure.adapter.in.web.controller.EnvioController;
import seb.com.msenvio.infrastructure.adapter.in.web.dto.envio.EnvioWebResponse;
import seb.com.msenvio.infrastructure.adapter.in.web.mapper.EnvioWebMapper;

import java.util.List;
import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(
        controllers = EnvioController.class,
        excludeAutoConfiguration = {
                SecurityAutoConfiguration.class,
                OAuth2ResourceServerAutoConfiguration.class,
                KafkaAutoConfiguration.class
        }
)
@AutoConfigureMockMvc(addFilters = false)
public class EnvioControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private EnvioInputPort envioInputPort;

    @MockitoBean
    private EnvioWebMapper envioWebMapper;

    @MockitoBean
    private JpaMetamodelMappingContext jpaMappingContext;

    @MockitoBean
    private CacheManager cacheManager;

    private final UUID ENVIO_ID = UUID.randomUUID();
    private final UUID REPARTIDOR_ID = UUID.randomUUID();

    @Test
    void obtenerTodosLosEnviosDisponibles_DebeRetornarPagina() throws Exception {
        when(envioInputPort.obtenerEnviosDisponibles(any())).thenReturn(new PageImpl<>(List.of(Envio.builder().build())));
        when(envioWebMapper.toResponse(any(Envio.class))).thenReturn(mock(EnvioWebResponse.class));

        mockMvc.perform(get("/api/v1/envios/disponibles")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content").isArray());
    }

    @Test
    void obtenerEnviosAsignados_DebeRetornarPagina() throws Exception {
        when(envioInputPort.obtenerEnviosAsignados(eq(REPARTIDOR_ID), any())).thenReturn(new PageImpl<>(List.of(Envio.builder().build())));
        when(envioWebMapper.toResponse(any(Envio.class))).thenReturn(mock(EnvioWebResponse.class));

        mockMvc.perform(get("/api/v1/envios/{repartidorid}/asignados", REPARTIDOR_ID)
                        .param("page", "0")
                        .param("size", "10"))
                .andExpect(status().isOk());
    }

    @Test
    void obtenerEnviosEnruta_DebeRetornarPagina() throws Exception {
        when(envioInputPort.obtenerEnviosEnRuta(eq(REPARTIDOR_ID), any())).thenReturn(new PageImpl<>(List.of(Envio.builder().build())));
        when(envioWebMapper.toResponse(any(Envio.class))).thenReturn(mock(EnvioWebResponse.class));

        mockMvc.perform(get("/api/v1/envios/{repartidorid}/enruta", REPARTIDOR_ID))
                .andExpect(status().isOk());
    }

    @Test
    void obtenerEnviosEntregados_DebeRetornarPagina() throws Exception {
        when(envioInputPort.obtenerEnviosEntregados(eq(REPARTIDOR_ID), any())).thenReturn(new PageImpl<>(List.of(Envio.builder().build())));
        when(envioWebMapper.toResponse(any(Envio.class))).thenReturn(mock(EnvioWebResponse.class));

        mockMvc.perform(get("/api/v1/envios/{repartidorid}/entregado", REPARTIDOR_ID))
                .andExpect(status().isOk());
    }

    @Test
    void obtenerTodosLosEnvios_DebeRetornarPagina() throws Exception {
        when(envioInputPort.obtenerTodosLosEnvios(any())).thenReturn(new PageImpl<>(List.of(Envio.builder().build())));
        when(envioWebMapper.toResponse(any(Envio.class))).thenReturn(mock(EnvioWebResponse.class));

        mockMvc.perform(get("/api/v1/envios"))
                .andExpect(status().isOk());
    }

    @Test
    void getEnvioById_DebeRetornarEnvio() throws Exception {
        when(envioInputPort.obtenerEnvio(ENVIO_ID)).thenReturn(Envio.builder().build());
        when(envioWebMapper.toResponse(any(Envio.class))).thenReturn(mock(EnvioWebResponse.class));

        mockMvc.perform(get("/api/v1/envios/{id}", ENVIO_ID))
                .andExpect(status().isOk());
    }

    @Test
    void asignarRepartidor_DebeActualizarEstadoYRetornar200Ok() throws Exception {
        Envio envioMock = Envio.builder().build();
        when(envioInputPort.asignarRepartidor(eq(ENVIO_ID), eq(REPARTIDOR_ID))).thenReturn(envioMock);
        when(envioInputPort.actualizarEstado(any(), eq(EstadoEnvio.EN_RUTA))).thenReturn(envioMock);
        when(envioWebMapper.toResponse(any(Envio.class))).thenReturn(mock(EnvioWebResponse.class));

        mockMvc.perform(post("/api/v1/envios/{id}/asignar", ENVIO_ID)
                        .param("repartidorId", REPARTIDOR_ID.toString()))
                .andExpect(status().isOk());
    }

    @Test
    void entregarPedido_DebeCambiarEstadoAEntregado() throws Exception {
        when(envioInputPort.actualizarEstado(ENVIO_ID, EstadoEnvio.ENTREGADO)).thenReturn(Envio.builder().build());
        when(envioWebMapper.toResponse(any(Envio.class))).thenReturn(mock(EnvioWebResponse.class));

        mockMvc.perform(post("/api/v1/envios/{id}/entregar", ENVIO_ID))
                .andExpect(status().isOk());
    }

    @Test
    void obtenerEstadoDeEnvios_DebeRetornarLista() throws Exception {
        when(envioInputPort.obtenerEstadosDeEnvios()).thenReturn(List.of("PENDIENTE", "EN_RUTA", "ENTREGADO"));

        mockMvc.perform(get("/api/v1/envios/estado-envios"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$[0]").value("PENDIENTE"));
    }

    @Test
    void obtenerEnviosPorEstado_DebeRetornarPagina() throws Exception {
        when(envioInputPort.obtenerEnviosPorEstado(eq("PENDIENTE"), any())).thenReturn(new PageImpl<>(List.of(Envio.builder().build())));
        when(envioWebMapper.toResponse(any(Envio.class))).thenReturn(mock(EnvioWebResponse.class));

        mockMvc.perform(get("/api/v1/envios/estado/{estado}", "PENDIENTE"))
                .andExpect(status().isOk());
    }
}
