package com.seb.msinventario.infrastructure.adapter.in.web.controller;

import com.seb.msinventario.application.port.in.StockInputPort;
import com.seb.msinventario.application.port.in.command.mapper.StockCommandMapper;
import com.seb.msinventario.application.port.in.command.stock.StockInputCommand;
import com.seb.msinventario.domain.model.Stock;
import com.seb.msinventario.infrastructure.adapter.in.web.dto.stock.StockWebRequest;
import com.seb.msinventario.infrastructure.adapter.in.web.dto.stock.StockWebResponse;
import com.seb.msinventario.infrastructure.adapter.in.web.mapper.StockWebMapper;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("api/v1/inventario/{inventarioId}/stock")
@AllArgsConstructor
public class StockController {
    private StockInputPort stockInputPort;
    private StockWebMapper  stockWebMapper;
    @GetMapping
    public ResponseEntity<List<StockWebResponse>> findAll(@PathVariable UUID inventarioId) {
        List<Stock> stockList = stockInputPort.obtenerStocks(inventarioId);
        List<StockWebResponse> stockWebResponseList = stockWebMapper.toResponseList(stockList);
        return ResponseEntity.ok(stockWebResponseList);
    }
    @GetMapping("/{id}")
    public ResponseEntity<StockWebResponse> findById(@PathVariable UUID inventarioId, @PathVariable UUID id) {
        Stock stock = stockInputPort.obtenerStock(inventarioId, id);
        StockWebResponse stockWebResponse = stockWebMapper.toResponse(stock);
        return ResponseEntity.ok(stockWebResponse);
    }
    @PostMapping
    public ResponseEntity<StockWebResponse> create(@PathVariable UUID inventarioId,@Valid @RequestBody StockWebRequest stockWebRequest) {
        StockInputCommand stockInputCommand = stockWebMapper.toCommand(stockWebRequest);
        StockWebResponse stockWebResponse = stockWebMapper.toResponse(stockInputPort.guardarStock(inventarioId,stockInputCommand));
        URI location = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}").buildAndExpand(stockWebResponse.stockId()).toUri();
        return ResponseEntity.created(location).body(stockWebResponse);
    }
    @PutMapping("/{id}")
    public ResponseEntity<StockWebResponse> update(@PathVariable UUID inventarioId,@PathVariable UUID id,
                                                   @Valid @RequestBody StockWebRequest stockWebRequest) {
        StockInputCommand stockInputCommand = stockWebMapper.toCommand(stockWebRequest);
        StockWebResponse stockWebResponse = stockWebMapper.toResponse(stockInputPort.actualizarStock(inventarioId,id,stockInputCommand));
        URI location = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}").buildAndExpand(stockWebResponse.stockId()).toUri();
        return ResponseEntity.created(location).body(stockWebResponse);
    }
    @DeleteMapping("/{id}")
    public ResponseEntity<StockWebResponse> delete(@PathVariable UUID inventarioId,@PathVariable UUID id) {
        stockInputPort.eliminarStock(inventarioId, id);
        return ResponseEntity.noContent().build();
    }
}
