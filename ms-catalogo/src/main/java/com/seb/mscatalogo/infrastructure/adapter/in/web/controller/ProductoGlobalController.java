package com.seb.mscatalogo.infrastructure.adapter.in.web.controller;

import com.seb.mscatalogo.application.port.in.ProductoInputPort;
import com.seb.mscatalogo.application.port.in.command.CategoriaProductoWebRequestCommand;
import com.seb.mscatalogo.infrastructure.adapter.in.web.dto.Producto.CategoriaProductoWebRequest;
import com.seb.mscatalogo.infrastructure.adapter.in.web.dto.Producto.ProductoWebResponse;
import com.seb.mscatalogo.infrastructure.adapter.in.web.mapper.ProductoWebMapper;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/vi/producto")
@AllArgsConstructor
public class ProductoGlobalController {
    private ProductoInputPort productoInputPort;
    private ProductoWebMapper  productoWebMapper;
    @PatchMapping("/cambiar-categoria")
    public ResponseEntity<ProductoWebResponse> patch(@Valid @RequestBody CategoriaProductoWebRequest categoriaProductoWebRequest){
        CategoriaProductoWebRequestCommand categoriaProductoWebRequestCommand= productoWebMapper.toWebRequestCommand(categoriaProductoWebRequest);
        ProductoWebResponse productoWebResponse = productoWebMapper.toWebResponse(productoInputPort.cambiarCategoria(categoriaProductoWebRequestCommand));
        return ResponseEntity.accepted().body(productoWebResponse);
    }
}
