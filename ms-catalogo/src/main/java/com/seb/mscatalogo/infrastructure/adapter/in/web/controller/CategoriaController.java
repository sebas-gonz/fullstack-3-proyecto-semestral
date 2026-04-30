package com.seb.mscatalogo.infrastructure.adapter.in.web.controller;
import com.seb.mscatalogo.application.port.in.CategoriaInputPort;
import com.seb.mscatalogo.application.port.in.command.CategoriaWebRequestCommand;
import com.seb.mscatalogo.domain.model.Categoria;
import com.seb.mscatalogo.infrastructure.adapter.in.web.dto.categoria.CategoriaWebRequest;
import com.seb.mscatalogo.infrastructure.adapter.in.web.dto.categoria.CategoriaWebResponse;
import com.seb.mscatalogo.infrastructure.adapter.in.web.mapper.CategoriaWebMapper;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("api/v1/categoria")
@AllArgsConstructor
public class CategoriaController {
    private final CategoriaInputPort categoriaInputPort;
    private final CategoriaWebMapper categoriaWebMapper;

    @GetMapping
    public ResponseEntity<List<CategoriaWebResponse>>  findAll() {
        List<CategoriaWebResponse> categoriaWebResponseList = categoriaWebMapper
                .toResponseList(categoriaInputPort.obtenerTodosCategorias());
        return ResponseEntity.ok().body(categoriaWebResponseList);
    }
    @PostMapping
    public ResponseEntity<CategoriaWebResponse> create(@Valid @RequestBody CategoriaWebRequest categoriaWebRequest) {
        CategoriaWebRequestCommand categoriaCommand = categoriaWebMapper.toCommand(categoriaWebRequest);
        CategoriaWebResponse categoriaWebResponse = categoriaWebMapper.toResponse(categoriaInputPort.crearCategoria(categoriaCommand));
        URI location = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}").buildAndExpand(categoriaWebResponse.id()).toUri();
        return ResponseEntity.created(location).body(categoriaWebResponse);
    }
    @GetMapping("/{id}")
    public ResponseEntity<CategoriaWebResponse> findById(@PathVariable UUID id) {
        CategoriaWebResponse categoriaWebResponse = categoriaWebMapper.toResponse(categoriaInputPort.obtenerCategoria(id));
        return ResponseEntity.ok().body(categoriaWebResponse);
    }
    @PutMapping("/{id}")
    public ResponseEntity<CategoriaWebResponse> update(@PathVariable UUID id, @RequestBody CategoriaWebRequest categoriaWebRequest) {
        CategoriaWebRequestCommand categoriaCommand = categoriaWebMapper.toCommand(categoriaWebRequest);
        CategoriaWebResponse categoriaWebResponse = categoriaWebMapper.toResponse(categoriaInputPort.actualizarCategoria(id, categoriaCommand));
        return ResponseEntity.accepted().body(categoriaWebResponse);
    }
    @DeleteMapping("/{id}")
    public ResponseEntity<?> delete(@PathVariable UUID id) {
        categoriaInputPort.eliminarCategoria(id);
        return ResponseEntity.noContent().build();
    }
}
