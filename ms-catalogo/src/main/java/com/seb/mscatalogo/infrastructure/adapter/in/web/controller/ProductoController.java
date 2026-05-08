package com.seb.mscatalogo.infrastructure.adapter.in.web.controller;

import com.seb.mscatalogo.application.port.in.ProductoInputPort;
import com.seb.mscatalogo.application.port.in.command.producto.ProductoWebRequestCommand;
import com.seb.mscatalogo.infrastructure.adapter.in.web.dto.Producto.ProductoWebRequest;
import com.seb.mscatalogo.infrastructure.adapter.in.web.dto.Producto.ProductoWebResponse;
import com.seb.mscatalogo.infrastructure.adapter.in.web.mapper.ProductoWebMapper;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("api/v1/categoria/{id}/producto")
@AllArgsConstructor
public class ProductoController {
    private final ProductoInputPort  productoInputPort;
    private final ProductoWebMapper productoWebMapper;

    @GetMapping
    public ResponseEntity<List<ProductoWebResponse>> findAll(@PathVariable UUID id){
        List<ProductoWebResponse> productoWebResponses = productoWebMapper.
                toWebResponseList(productoInputPort.obtenerProductoPorCategoriaId(id));
        return ResponseEntity.ok().body(productoWebResponses);
    }
    @PostMapping
    public ResponseEntity<ProductoWebResponse> create(@PathVariable UUID id,@Valid @RequestBody ProductoWebRequest productoWebRequest){
        ProductoWebRequestCommand productoWebRequestCommand = productoWebMapper
                .toWebCommand(productoWebRequest);
        ProductoWebResponse productoWebResponse = productoWebMapper
                .toWebResponse(productoInputPort.crearProducto(id, productoWebRequestCommand));
        URI location = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}")
                .buildAndExpand(productoWebResponse.productoId()).toUri();
        return ResponseEntity.created(location).body(productoWebResponse);
    }
    @PutMapping("/{productoid}")
    public ResponseEntity<ProductoWebResponse> update(@PathVariable UUID productoid,@Valid @RequestBody ProductoWebRequest productoWebRequest){
        ProductoWebRequestCommand productoWebRequestCommand = productoWebMapper
                .toWebCommand(productoWebRequest);
        ProductoWebResponse productoWebResponse = productoWebMapper.toWebResponse(productoInputPort.actualizarProducto(productoid, productoWebRequestCommand));
        return ResponseEntity.accepted().body(productoWebResponse);
    }
    @DeleteMapping("/{productoid}")
    public ResponseEntity<?> delete(@PathVariable UUID productoid){
        productoInputPort.eliminarProducto(productoid);
        return ResponseEntity.noContent().build();
    }
}
