package com.seb.msbffweb.controller;

import com.seb.msbffweb.client.InventarioClient;
import com.seb.msbffweb.client.ProductoClient;
import com.seb.msbffweb.dto.out.producto.ProductoWebResponse;
import com.seb.msbffweb.dto.in.stock.StockWebResponse;
import com.seb.msbffweb.dto.out.stock.StockDetalladoResponse;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/bff/v1/inventarios")
@AllArgsConstructor
public class StockBffController {
    private final InventarioClient inventarioClient;
    private final ProductoClient productoClient;

    @GetMapping("/{inventarioid}/stocks-detallados")
    public ResponseEntity<List<StockDetalladoResponse>> obtenerStockDetallados(@PathVariable("inventarioid") UUID inventarioid) {
        List<StockWebResponse> stockWebResponseList = inventarioClient.findAll(inventarioid);
        List<StockDetalladoResponse> stockDetalladoResponses = stockWebResponseList.stream()
                .map( stock -> {
                    ProductoWebResponse productoWebResponse = productoClient.obtenerProductoPorId(stock.productoId());
                    return new StockDetalladoResponse(stock.stockId(),stock.lote(),stock.cantidad(),productoWebResponse.nombre(),
                            productoWebResponse.sku(),productoWebResponse.productoId(),productoWebResponse.fechaRegistro());
                }).collect(Collectors.toList());
        return ResponseEntity.ok(stockDetalladoResponses);
    }
}
