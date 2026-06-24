package com.seb.msusuario;

import com.seb.msusuario.application.port.in.UsuarioInputPort;
import com.seb.msusuario.application.port.in.command.CrearUsuarioCommand;
import com.seb.msusuario.infrastructure.adapter.in.web.controller.UsuarioController;
import com.seb.msusuario.infrastructure.adapter.in.web.dto.usuario.RepartidorRestResponseDto;
import com.seb.msusuario.infrastructure.adapter.in.web.dto.usuario.UsuarioResponse;
import com.seb.msusuario.infrastructure.adapter.in.web.mapper.UsuarioWebMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.security.autoconfigure.SecurityAutoConfiguration;
import org.springframework.boot.security.oauth2.server.resource.autoconfigure.servlet.OAuth2ResourceServerAutoConfiguration;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
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
        controllers = UsuarioController.class,
        excludeAutoConfiguration = {
                SecurityAutoConfiguration.class,
                OAuth2ResourceServerAutoConfiguration.class
        }
)
@AutoConfigureMockMvc(addFilters = false)
public class UsuarioControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private UsuarioInputPort  usuarioInputPort;

    @MockitoBean
    private UsuarioWebMapper usuarioWebMapper;

    @MockitoBean
    private JpaMetamodelMappingContext jpaMappingContext;

    @Test
    void obtenerTodosLosUsuarios() throws Exception {
        when(usuarioInputPort.obtenerTodosUsuarios()).thenReturn(List.of());
        when(usuarioWebMapper.toResponseList(any())).thenReturn(List.of());

        mockMvc.perform(get("/api/v1/usuarios")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray());
    }

    @Test
    void crearUsuario() throws Exception {
        UUID nuevoId = UUID.randomUUID();
        UsuarioResponse mockResponse = mock(UsuarioResponse.class);
        when(mockResponse.usuarioId()).thenReturn(nuevoId);

        when(usuarioWebMapper.toCommand(any())).thenReturn(mock(CrearUsuarioCommand.class));
        when(usuarioInputPort.crearUsuario(any())).thenReturn(null);
        when(usuarioWebMapper.toResponse(any())).thenReturn(mockResponse);
        String requestJson = "{\"nombre\":\"Juan\", \"apellido\":\"Perez\", \"email\":\"juan@test.com\", \"rol\":\"USER\"}";

        mockMvc.perform(post("/api/v1/usuarios")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestJson))
                .andExpect(status().isCreated())
                .andExpect(header().exists("Location"))
                .andExpect(header().string("Location", containsString(nuevoId.toString())));
    }

    @Test
    void obtenerUsuarioPorId() throws Exception {
        UUID id = UUID.randomUUID();
        when(usuarioInputPort.obtenerUsuarioPorId(id)).thenReturn(null);
        UsuarioResponse mockResponse = mock(UsuarioResponse.class);
        when(usuarioWebMapper.toResponse(any())).thenReturn(mockResponse);

        mockMvc.perform(get("/api/v1/usuarios/{id}", id)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }
    @Test
    void editarUsuario() throws Exception {
        UUID id = UUID.randomUUID();
        when(usuarioWebMapper.toCommand(any())).thenReturn(org.mockito.Mockito.mock(CrearUsuarioCommand.class));
        when(usuarioInputPort.actualizarUsuario(eq(id), any())).thenReturn(null);
        when(usuarioWebMapper.toResponse(any())).thenReturn(org.mockito.Mockito.mock(UsuarioResponse.class));

        String requestJson = "{\"nombre\":\"Juan Editado\", \"apellido\":\"Perez\", \"email\":\"juan@test.com\", \"rol\":\"USER\"}";
        mockMvc.perform(put("/api/v1/usuarios/{id}", id)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestJson))
                .andExpect(status().isAccepted());
    }
    @Test
    void eliminarUsuario() throws Exception {
        UUID id = UUID.randomUUID();
        doNothing().when(usuarioInputPort).eliminarUsuario(id);
        mockMvc.perform(delete("/api/v1/usuarios/{id}", id)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent());
    }
    @Test
    void obtenerRepartidores_DebeRetornarPaginaDeRepartidores() throws Exception {
        RepartidorRestResponseDto repartidorMock = org.mockito.Mockito.mock(RepartidorRestResponseDto.class);
        Page<RepartidorRestResponseDto> paginaMock = new PageImpl<>(List.of(repartidorMock));

        when(usuarioInputPort.obtenerRepartidores(any(Pageable.class))).thenReturn(new PageImpl<>(List.of()));
        when(usuarioWebMapper.toRepartidorResponseDto(any())).thenReturn(repartidorMock);
        mockMvc.perform(get("/api/v1/usuarios/repartidores")
                        .param("page", "0")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content").isArray());
    }
}
