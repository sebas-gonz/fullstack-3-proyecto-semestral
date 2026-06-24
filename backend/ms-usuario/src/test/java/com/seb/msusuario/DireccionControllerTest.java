package com.seb.msusuario;

import com.seb.msusuario.application.port.in.DireccionInputPort;
import com.seb.msusuario.application.port.in.command.CrearUbicacionCommand;
import com.seb.msusuario.domain.model.Ubicacion;
import com.seb.msusuario.infrastructure.adapter.in.web.controller.DireccionController;
import com.seb.msusuario.infrastructure.adapter.in.web.dto.direccion.DireccionResponse;
import com.seb.msusuario.infrastructure.adapter.in.web.mapper.DireccionWebMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.security.autoconfigure.SecurityAutoConfiguration;
import org.springframework.boot.security.oauth2.server.resource.autoconfigure.servlet.OAuth2ResourceServerAutoConfiguration;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.data.jpa.mapping.JpaMetamodelMappingContext;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.UUID;

import static org.hamcrest.Matchers.containsString;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(
        controllers = DireccionController.class,
        excludeAutoConfiguration = {
                SecurityAutoConfiguration.class,
                OAuth2ResourceServerAutoConfiguration.class
        }
)
@AutoConfigureMockMvc(addFilters = false)
public class DireccionControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private DireccionInputPort direccionInputPort;

    @MockitoBean
    private DireccionWebMapper direccionWebMapper;

    @MockitoBean
    private JpaMetamodelMappingContext jpaMappingContext;

    private final UUID USUARIO_ID = UUID.randomUUID();
    private final UUID DIRECCION_ID = UUID.randomUUID();

    private final String requestJson = "{\"calle\":\"Av. Siempre Viva\", \"numero\":\"123\", \"ciudad\":\"Springfield\", \"pais\":\"USA\"}";

    @Test
    void agregarDireccion_DebeRetornar201Created() throws Exception {
        DireccionResponse mockResponse = org.mockito.Mockito.mock(DireccionResponse.class);
        when(mockResponse.ubicacionId()).thenReturn(DIRECCION_ID);

        when(direccionWebMapper.toCommand(any())).thenReturn(org.mockito.Mockito.mock(CrearUbicacionCommand.class));
        when(direccionInputPort.agregarDireccion(eq(USUARIO_ID), any())).thenReturn(new Ubicacion());
        when(direccionWebMapper.toResponse(any())).thenReturn(mockResponse);

        mockMvc.perform(post("/api/v1/usuarios/{id}/ubicaciones", USUARIO_ID)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestJson))
                .andExpect(status().isCreated())
                .andExpect(header().exists("Location"))
                .andExpect(header().string("Location", containsString(DIRECCION_ID.toString())));
    }

    @Test
    void obtenerDireccion_DebeRetornar200Ok() throws Exception {
        when(direccionInputPort.obtenerDireccionPorId(DIRECCION_ID)).thenReturn(new Ubicacion());
        when(direccionWebMapper.toResponse(any())).thenReturn(org.mockito.Mockito.mock(DireccionResponse.class));

        mockMvc.perform(get("/api/v1/usuarios/{id}/ubicaciones/{direccionId}", USUARIO_ID, DIRECCION_ID)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    void actualizarDireccion_DebeRetornar202Accepted() throws Exception {
        when(direccionWebMapper.toCommand(any())).thenReturn(org.mockito.Mockito.mock(CrearUbicacionCommand.class));
        when(direccionInputPort.actualizarDireccion(eq(USUARIO_ID), eq(DIRECCION_ID), any())).thenReturn(new Ubicacion());
        when(direccionWebMapper.toResponse(any())).thenReturn(org.mockito.Mockito.mock(DireccionResponse.class));

        mockMvc.perform(put("/api/v1/usuarios/{id}/ubicaciones/update/{direccionId}", USUARIO_ID, DIRECCION_ID)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestJson))
                .andExpect(status().isAccepted());
    }

    @Test
    void eliminarDireccion_DebeRetornar204NoContent() throws Exception {
        doNothing().when(direccionInputPort).eliminarDireccion(USUARIO_ID, DIRECCION_ID);

        mockMvc.perform(delete("/api/v1/usuarios/{id}/ubicaciones/delete/{direccionId}", USUARIO_ID, DIRECCION_ID)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent());
    }
}

