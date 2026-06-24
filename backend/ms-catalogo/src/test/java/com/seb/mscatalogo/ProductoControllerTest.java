package com.seb.mscatalogo;

import com.seb.mscatalogo.application.port.in.ProductoInputPort;
import com.seb.mscatalogo.application.port.in.command.producto.ProductoWebRequestCommand;
import com.seb.mscatalogo.domain.model.Producto;
import com.seb.mscatalogo.infrastructure.adapter.in.web.controller.ProductoController;
import com.seb.mscatalogo.infrastructure.adapter.in.web.dto.Producto.ProductoWebResponse;
import com.seb.mscatalogo.infrastructure.adapter.in.web.mapper.ProductoWebMapper;
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

import java.math.BigDecimal;
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
        controllers = ProductoController.class,
        excludeAutoConfiguration = {
                SecurityAutoConfiguration.class,
                OAuth2ResourceServerAutoConfiguration.class,
                KafkaAutoConfiguration.class
        }
)
@AutoConfigureMockMvc(addFilters = false)
public class ProductoControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private ProductoInputPort productoInputPort;

    @MockitoBean
    private ProductoWebMapper productoWebMapper;

    @MockitoBean
    private JpaMetamodelMappingContext jpaMappingContext;

    @MockitoBean
    private CacheManager cacheManager;

    private final UUID CATEGORIA_ID = UUID.randomUUID();
    private final UUID PRODUCTO_ID = UUID.randomUUID();
    private final String productoRequestJson = """
            {
              "sku": "LAP-001",
              "nombre": "Laptop Pro",
              "descripcion": "Laptop de alto rendimiento",
              "precioBase": 1500.50
            }
            """;
    private final ProductoWebResponse responseReal = new ProductoWebResponse(
            PRODUCTO_ID,
            "LAP-001",
            "Laptop Pro",
            "Laptop de alto rendimiento",
            new BigDecimal("1500.50"),
            100,
            Instant.now()
    );

    @Test
    void findAll_DebeRetornarLista200Ok() throws Exception {
        when(productoInputPort.obtenerProductoPorCategoriaId(CATEGORIA_ID)).thenReturn(List.of(new Producto()));
        when(productoWebMapper.toWebResponseList(any())).thenReturn(List.of(responseReal));

        mockMvc.perform(get("/api/v1/categorias/{id}/producto", CATEGORIA_ID)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$[0].sku").value("LAP-001"));
    }

    @Test
    void create_DebeRetornar201CreatedYLocation() throws Exception {
        when(productoWebMapper.toWebCommand(any())).thenReturn(new ProductoWebRequestCommand("LAP-001", "Laptop Pro", "Desc", new BigDecimal("1500.50")));
        when(productoInputPort.crearProducto(eq(CATEGORIA_ID), any())).thenReturn(new Producto());
        when(productoWebMapper.toWebResponse(any())).thenReturn(responseReal);

        mockMvc.perform(post("/api/v1/categorias/{id}/producto", CATEGORIA_ID)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(productoRequestJson))
                .andExpect(status().isCreated())
                .andExpect(header().exists("Location"))
                .andExpect(header().string("Location", containsString(PRODUCTO_ID.toString())));
    }

    @Test
    void update_DebeRetornar202Accepted() throws Exception {
        when(productoWebMapper.toWebCommand(any())).thenReturn(new ProductoWebRequestCommand("LAP-001", "Laptop Pro", "Desc", new BigDecimal("1500.50")));
        when(productoInputPort.actualizarProducto(eq(PRODUCTO_ID), any())).thenReturn(new Producto());
        when(productoWebMapper.toWebResponse(any())).thenReturn(responseReal);
        mockMvc.perform(put("/api/v1/categorias/{id}/producto/{productoid}", CATEGORIA_ID, PRODUCTO_ID)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(productoRequestJson))
                .andExpect(status().isAccepted())
                .andExpect(jsonPath("$.nombre").value("Laptop Pro"));
    }

    @Test
    void delete_DebeRetornar204NoContent() throws Exception {
        doNothing().when(productoInputPort).eliminarProducto(PRODUCTO_ID);

        mockMvc.perform(delete("/api/v1/categorias/{id}/producto/{productoid}", CATEGORIA_ID, PRODUCTO_ID)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent());
    }

    @Test
    void obtenerProductosDisponibles_DebeRetornarLista200Ok() throws Exception {
        when(productoInputPort.obtenerProductosDisponibles(CATEGORIA_ID)).thenReturn(List.of(new Producto()));
        when(productoWebMapper.toWebResponseList(any())).thenReturn(List.of(responseReal));

        mockMvc.perform(get("/api/v1/categorias/{id}/producto/disponibles", CATEGORIA_ID)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$[0].sku").value("LAP-001"));
    }
}
