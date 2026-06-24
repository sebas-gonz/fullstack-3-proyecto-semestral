package com.seb.msinventario;

import com.seb.msinventario.application.port.in.InventarioInputPort;
import com.seb.msinventario.application.port.in.command.inventario.InventarioInputCommand;
import com.seb.msinventario.domain.model.Inventario;
import com.seb.msinventario.infrastructure.adapter.in.web.controller.InventarioController;
import com.seb.msinventario.infrastructure.adapter.in.web.dto.inventario.InventarioWebResponse;
import com.seb.msinventario.infrastructure.adapter.in.web.dto.ubicacion.UbicacionWebResponse;
import com.seb.msinventario.infrastructure.adapter.in.web.mapper.InventarioWebMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.kafka.autoconfigure.KafkaAutoConfiguration;
import org.springframework.boot.security.autoconfigure.SecurityAutoConfiguration;
import org.springframework.boot.security.oauth2.server.resource.autoconfigure.servlet.OAuth2ResourceServerAutoConfiguration;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.data.jpa.mapping.JpaMetamodelMappingContext;
import org.springframework.http.MediaType;
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
        controllers = InventarioController.class,
        excludeAutoConfiguration = {
                SecurityAutoConfiguration.class,
                OAuth2ResourceServerAutoConfiguration.class,
                KafkaAutoConfiguration.class
        }
)
@AutoConfigureMockMvc(addFilters = false)
public class InventarioControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private InventarioInputPort inventarioInputPort;

    @MockitoBean
    private InventarioWebMapper inventarioWebMapper;

    @MockitoBean
    private JpaMetamodelMappingContext jpaMappingContext;

    private final UUID INVENTARIO_ID = UUID.randomUUID();
    private final UUID PRODUCTO_ID = UUID.randomUUID();
    private final String inventarioRequestJson = """
            {
              "nombre": "Bodega Central Rancagua",
              "ubicacion": {
                "calle": "Av. San Martín",
                "numero": "234",
                "ciudad": "Rancagua",
                "latitude": -34.1708,
                "longitude": -70.7444
              }
            }
            """;

    @Test
    void findAll_DebeRetornarLista200Ok() throws Exception {
        when(inventarioInputPort.obtenerInventarios()).thenReturn(List.of(new Inventario()));
        when(inventarioWebMapper.toInventarioResponseList(any())).thenReturn(List.of(mock(InventarioWebResponse.class)));

        mockMvc.perform(get("/api/v1/inventarios")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray());
    }

    @Test
    void findById_DebeRetornar200Ok() throws Exception {
        when(inventarioInputPort.obtenerInventario(INVENTARIO_ID)).thenReturn(new Inventario());
        when(inventarioWebMapper.toResponse(any(Inventario.class))).thenReturn(mock(InventarioWebResponse.class));

        mockMvc.perform(get("/api/v1/inventarios/{id}", INVENTARIO_ID)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    void obtenerUbicacion_DebeRetornar200Ok() throws Exception {
        UbicacionWebResponse ubicacionReal = new UbicacionWebResponse(
                "Calle", "123", "Ciudad", java.math.BigDecimal.ZERO, java.math.BigDecimal.ZERO
        );
        InventarioWebResponse responseMock = mock(InventarioWebResponse.class);

        when(inventarioInputPort.obtenerInventario(INVENTARIO_ID)).thenReturn(new Inventario());
        when(inventarioWebMapper.toResponse(any(Inventario.class))).thenReturn(responseMock);
        when(responseMock.ubicacion()).thenReturn(ubicacionReal);

        mockMvc.perform(get("/api/v1/inventarios/{id}/ubicacion", INVENTARIO_ID)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    void create_DebeRetornar201CreatedYLocation() throws Exception {
        Inventario inventarioMock = mock(Inventario.class);
        when(inventarioMock.getInventarioId()).thenReturn(INVENTARIO_ID);

        when(inventarioWebMapper.toCommand(any())).thenReturn(mock(InventarioInputCommand.class));
        when(inventarioInputPort.guardarInventario(any(InventarioInputCommand.class))).thenReturn(inventarioMock);
        when(inventarioWebMapper.toResponse(any(Inventario.class))).thenReturn(mock(InventarioWebResponse.class));

        mockMvc.perform(post("/api/v1/inventarios")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(inventarioRequestJson))
                .andExpect(status().isCreated())
                .andExpect(header().exists("Location"))
                .andExpect(header().string("Location", containsString(INVENTARIO_ID.toString())));
    }

    @Test
    void update_DebeRetornar201CreatedYLocation() throws Exception {
        Inventario inventarioMock = mock(Inventario.class);
        when(inventarioMock.getInventarioId()).thenReturn(INVENTARIO_ID);

        when(inventarioWebMapper.toCommand(any())).thenReturn(mock(InventarioInputCommand.class));
        when(inventarioInputPort.actualizarInventario(eq(INVENTARIO_ID), any())).thenReturn(inventarioMock);
        when(inventarioWebMapper.toResponse(any(Inventario.class))).thenReturn(mock(InventarioWebResponse.class));

        mockMvc.perform(put("/api/v1/inventarios/{id}", INVENTARIO_ID)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(inventarioRequestJson))
                .andExpect(status().isCreated())
                .andExpect(header().exists("Location"))
                .andExpect(header().string("Location", containsString(INVENTARIO_ID.toString())));
    }

    @Test
    void delete_DebeRetornar204NoContent() throws Exception {
        doNothing().when(inventarioInputPort).eliminarInventario(INVENTARIO_ID);

        mockMvc.perform(delete("/api/v1/inventarios/{id}", INVENTARIO_ID)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent());
    }

    @Test
    void obtenerInventarioPorProducto_DebeRetornarLista200Ok() throws Exception {
        when(inventarioInputPort.obtenerInventariosPorProducto(PRODUCTO_ID)).thenReturn(List.of(new Inventario()));
        when(inventarioWebMapper.toInventarioResponseList(any())).thenReturn(List.of(mock(InventarioWebResponse.class)));

        mockMvc.perform(get("/api/v1/inventarios/producto/{productoid}", PRODUCTO_ID)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray());
    }
}
