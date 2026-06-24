package com.seb.msinventario;

import com.seb.msinventario.application.port.in.StockInputPort;
import com.seb.msinventario.application.port.in.command.stock.StockInputCommand;
import com.seb.msinventario.domain.model.Stock;
import com.seb.msinventario.infrastructure.adapter.in.web.controller.StockController;
import com.seb.msinventario.infrastructure.adapter.in.web.dto.stock.StockWebResponse;
import com.seb.msinventario.infrastructure.adapter.in.web.mapper.StockWebMapper;
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
        controllers = StockController.class,
        excludeAutoConfiguration = {
                SecurityAutoConfiguration.class,
                OAuth2ResourceServerAutoConfiguration.class,
                KafkaAutoConfiguration.class
        }
)
@AutoConfigureMockMvc(addFilters = false)
public class StockControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private StockInputPort stockInputPort;

    @MockitoBean
    private StockWebMapper stockWebMapper;

    @MockitoBean
    private JpaMetamodelMappingContext jpaMappingContext;

    @MockitoBean
    private CacheManager cacheManager;

    private final UUID INVENTARIO_ID = UUID.randomUUID();
    private final UUID STOCK_ID = UUID.randomUUID();
    private final UUID PRODUCTO_ID = UUID.randomUUID();

    // Payload genérico para POST y PUT
    private final String stockRequestJson = """
            {
              "productoId": "123e4567-e89b-12d3-a456-426614174000",
              "lote": "LOTE-2026-A",
              "cantidad": 150,
              "fechaRegistroLote": "2026-06-22T00:00:00Z"
            }
            """;

    @Test
    void findAll_DebeRetornarLista200Ok() throws Exception {
        when(stockInputPort.obtenerStocks(INVENTARIO_ID)).thenReturn(List.of(new Stock()));

        StockWebResponse responseReal = new StockWebResponse(STOCK_ID, PRODUCTO_ID, "LOTE-1", 10, Instant.now(), Instant.now());
        when(stockWebMapper.toResponseList(any())).thenReturn(List.of(responseReal));

        mockMvc.perform(get("/api/v1/inventarios/{inventarioId}/stock", INVENTARIO_ID)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray());
    }

    @Test
    void findById_DebeRetornar200Ok() throws Exception {
        when(stockInputPort.obtenerStock(INVENTARIO_ID, STOCK_ID)).thenReturn(new Stock());

        StockWebResponse responseReal = new StockWebResponse(STOCK_ID, PRODUCTO_ID, "LOTE-1", 10, Instant.now(), Instant.now());
        when(stockWebMapper.toResponse(any(Stock.class))).thenReturn(responseReal);

        mockMvc.perform(get("/api/v1/inventarios/{inventarioId}/stock/{id}", INVENTARIO_ID, STOCK_ID)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.stockId").value(STOCK_ID.toString()));
    }

    @Test
    void create_DebeRetornar201CreatedYLocation() throws Exception {
        // Instanciamos el record con un ID válido para que el Location Builder no falle
        StockWebResponse responseReal = new StockWebResponse(STOCK_ID, PRODUCTO_ID, "LOTE-1", 150, Instant.now(), Instant.now());

        when(stockWebMapper.toCommand(any())).thenReturn(new StockInputCommand(PRODUCTO_ID, "LOTE-1", 150, Instant.now()));
        when(stockInputPort.guardarStock(eq(INVENTARIO_ID), any())).thenReturn(new Stock());
        when(stockWebMapper.toResponse(any(Stock.class))).thenReturn(responseReal);

        mockMvc.perform(post("/api/v1/inventarios/{inventarioId}/stock", INVENTARIO_ID)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(stockRequestJson))
                .andExpect(status().isCreated())
                .andExpect(header().exists("Location"))
                .andExpect(header().string("Location", containsString(STOCK_ID.toString())));
    }

    @Test
    void update_DebeRetornar201CreatedYLocation() throws Exception {
        StockWebResponse responseReal = new StockWebResponse(STOCK_ID, PRODUCTO_ID, "LOTE-1", 150, Instant.now(), Instant.now());

        when(stockWebMapper.toCommand(any())).thenReturn(new StockInputCommand(PRODUCTO_ID, "LOTE-1", 150, Instant.now()));
        when(stockInputPort.actualizarStock(eq(INVENTARIO_ID), eq(STOCK_ID), any())).thenReturn(new Stock());
        when(stockWebMapper.toResponse(any(Stock.class))).thenReturn(responseReal);

        mockMvc.perform(put("/api/v1/inventarios/{inventarioId}/stock/{id}", INVENTARIO_ID, STOCK_ID)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(stockRequestJson))
                .andExpect(status().isCreated())
                .andExpect(header().exists("Location"))
                .andExpect(header().string("Location", containsString(STOCK_ID.toString())));
    }

    @Test
    void delete_DebeRetornar204NoContent() throws Exception {
        doNothing().when(stockInputPort).eliminarStock(INVENTARIO_ID, STOCK_ID);

        mockMvc.perform(delete("/api/v1/inventarios/{inventarioId}/stock/{id}", INVENTARIO_ID, STOCK_ID)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent());
    }
}
