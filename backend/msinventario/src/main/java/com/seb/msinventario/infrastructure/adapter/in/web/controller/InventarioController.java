package com.seb.msinventario.infrastructure.adapter.in.web.controller;

import com.seb.msinventario.application.port.in.InventarioInputPort;
import com.seb.msinventario.application.port.in.command.inventario.InventarioInputCommand;
import com.seb.msinventario.domain.model.Inventario;
import com.seb.msinventario.infrastructure.adapter.in.web.dto.inventario.InventarioWebRequest;
import com.seb.msinventario.infrastructure.adapter.in.web.dto.inventario.InventarioWebResponse;
import com.seb.msinventario.infrastructure.adapter.in.web.dto.ubicacion.UbicacionWebResponse;
import com.seb.msinventario.infrastructure.adapter.in.web.mapper.InventarioWebMapper;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/inventarios")
@AllArgsConstructor
public class InventarioController {
    private final InventarioInputPort inventarioInputPort;
    private final InventarioWebMapper inventarioWebMapper;
    @GetMapping
    public ResponseEntity<List<InventarioWebResponse>> findAll() {
        List<Inventario> inventarios = inventarioInputPort.obtenerInventarios();
        List<InventarioWebResponse> inventarioWebResponseList = inventarioWebMapper.toInventarioResponseList(inventarios);
        return ResponseEntity.ok().body(inventarioWebResponseList);
    }
    @GetMapping("/{id}")
    public ResponseEntity<InventarioWebResponse> findById(@PathVariable UUID id) {
        Inventario inventario = inventarioInputPort.obtenerInventario(id);
        InventarioWebResponse inventarioWebResponse = inventarioWebMapper.toResponse(inventario);
        return ResponseEntity.ok().body(inventarioWebResponse);
    }
    @GetMapping("/{id}/ubicacion")
    public ResponseEntity<UbicacionWebResponse> obtenerUbicacion(@PathVariable UUID id) {
        Inventario inventario = inventarioInputPort.obtenerInventario(id);
        UbicacionWebResponse ubicacionWebResponse = inventarioWebMapper.toResponse(inventario).ubicacion();
        return ResponseEntity.ok().body(ubicacionWebResponse);
    }
    @PostMapping
    public ResponseEntity<InventarioWebResponse> create(@Valid  @RequestBody InventarioWebRequest inventarioWebRequest) {
        InventarioInputCommand inventarioInputCommand = inventarioWebMapper.toCommand(inventarioWebRequest);
        Inventario inventario = inventarioInputPort.guardarInventario(inventarioInputCommand);
        InventarioWebResponse inventarioWebResponse = inventarioWebMapper.toResponse(inventario);
        URI location = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}").buildAndExpand(inventario.getInventarioId()).toUri();
        return ResponseEntity.created(location).body(inventarioWebResponse);
    }
    @PutMapping("/{id}")
    public ResponseEntity<InventarioWebResponse> update(@PathVariable UUID id,@Valid @RequestBody InventarioWebRequest inventarioWebRequest) {
        InventarioInputCommand inventarioInputCommand = inventarioWebMapper.toCommand(inventarioWebRequest);
        Inventario inventario = inventarioInputPort.actualizarInventario(id,inventarioInputCommand);
        InventarioWebResponse inventarioWebResponse = inventarioWebMapper.toResponse(inventario);
        URI location = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}").buildAndExpand(inventario.getInventarioId()).toUri();
        return ResponseEntity.created(location).body(inventarioWebResponse);
    }
    @DeleteMapping("/{id}")
    public ResponseEntity<?> delete(@PathVariable UUID id) {
        inventarioInputPort.eliminarInventario(id);
        return ResponseEntity.noContent().build();
    }
    @GetMapping("/producto/{productoid}")
    public ResponseEntity<List<InventarioWebResponse>> obtenerInventarioPorProducto(@PathVariable UUID productoid) {
        List<InventarioWebResponse> responses = inventarioWebMapper.toInventarioResponseList(inventarioInputPort.obtenerInventariosPorProducto(productoid));
        return ResponseEntity.ok(responses);
    }
}
