package com.seb.msbffweb.client;

import com.seb.msbffweb.dto.out.producto.ProductoWebResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.UUID;
@FeignClient(name = "ms-catalogo")
public interface ProductoClient {
        @GetMapping("/api/v1/producto/{id}")
        ProductoWebResponse obtenerProductoPorId(@PathVariable UUID id);

}
