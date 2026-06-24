package com.seb.mscatalogo;

import com.seb.mscatalogo.application.port.in.CategoriaInputPort;
import com.seb.mscatalogo.application.port.in.command.categoria.CategoriaWebRequestCommand;
import com.seb.mscatalogo.domain.model.Categoria;
import com.seb.mscatalogo.infrastructure.adapter.in.web.controller.CategoriaController;
import com.seb.mscatalogo.infrastructure.adapter.in.web.dto.categoria.CategoriaWebResponse;
import com.seb.mscatalogo.infrastructure.adapter.in.web.mapper.CategoriaWebMapper;
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

import java.time.Instant;
import java.util.List;
import java.util.UUID;

import static org.hamcrest.Matchers.containsString;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(
        controllers = CategoriaController.class,
        excludeAutoConfiguration = {
                SecurityAutoConfiguration.class,
                OAuth2ResourceServerAutoConfiguration.class,
                KafkaAutoConfiguration.class
        }
)
@AutoConfigureMockMvc(addFilters = false)
public class CategoriaControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private CategoriaInputPort categoriaInputPort;

    @MockitoBean
    private CategoriaWebMapper categoriaWebMapper;

    @MockitoBean
    private JpaMetamodelMappingContext jpaMappingContext;

    @MockitoBean
    private CacheManager cacheManager;

    private final UUID CATEGORIA_ID = UUID.randomUUID();
    private final String categoriaRequestJson = """
            {
              "nombre": "Tecnología",
              "descripcion": "Artículos electrónicos"
            }
            """;

    private final CategoriaWebResponse responseReal = new CategoriaWebResponse(
            CATEGORIA_ID,
            "Tecnología",
            "Artículos electrónicos",
            List.of(),
            Instant.now()
    );

    @Test
    void findAll_DebeRetornarLista200Ok() throws Exception {
        when(categoriaInputPort.obtenerTodosCategorias()).thenReturn(List.of(new Categoria()));
        when(categoriaWebMapper.toResponseList(any())).thenReturn(List.of(responseReal));

        mockMvc.perform(get("/api/v1/categorias")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$[0].nombre").value("Tecnología"));
    }

    @Test
    void findById_DebeRetornar200Ok() throws Exception {
        when(categoriaInputPort.obtenerCategoria(CATEGORIA_ID)).thenReturn(new Categoria());
        when(categoriaWebMapper.toResponse(any())).thenReturn(responseReal);

        mockMvc.perform(get("/api/v1/categorias/{id}", CATEGORIA_ID)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.categoriaId").value(CATEGORIA_ID.toString()));
    }

    @Test
    void create_DebeRetornar201CreatedYLocation() throws Exception {
        when(categoriaWebMapper.toCommand(any())).thenReturn(new CategoriaWebRequestCommand("Tecnología", "Artículos electrónicos"));
        when(categoriaInputPort.crearCategoria(any())).thenReturn(new Categoria());
        when(categoriaWebMapper.toResponse(any())).thenReturn(responseReal);

        mockMvc.perform(post("/api/v1/categorias")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(categoriaRequestJson))
                .andExpect(status().isCreated())
                .andExpect(header().exists("Location"))
                .andExpect(header().string("Location", containsString(CATEGORIA_ID.toString())));
    }

    @Test
    void update_DebeRetornar202Accepted() throws Exception {
        when(categoriaWebMapper.toCommand(any())).thenReturn(new CategoriaWebRequestCommand("Tecnología", "Artículos electrónicos"));
        when(categoriaInputPort.actualizarCategoria(eq(CATEGORIA_ID), any())).thenReturn(new Categoria());
        when(categoriaWebMapper.toResponse(any())).thenReturn(responseReal);

        mockMvc.perform(put("/api/v1/categorias/{id}", CATEGORIA_ID)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(categoriaRequestJson))
                .andExpect(status().isAccepted());
    }

    @Test
    void delete_DebeRetornar204NoContent() throws Exception {
        doNothing().when(categoriaInputPort).eliminarCategoria(CATEGORIA_ID);

        mockMvc.perform(delete("/api/v1/categorias/{id}", CATEGORIA_ID)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent());
    }

}
