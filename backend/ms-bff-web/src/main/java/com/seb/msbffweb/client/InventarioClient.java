package com.seb.msbffweb.client;

import com.seb.msbffweb.dto.in.stock.StockWebResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;
import java.util.UUID;

@FeignClient(name = "ms-inventario")
public interface InventarioClient {
    @GetMapping("/api/v1/inventarios/{id}/stock")
    List<StockWebResponse> findAll(@PathVariable UUID id);
}
