package com.seb.msusuario.infrastructure.adapter.in.web.controller;

import com.seb.msusuario.application.port.in.DireccionInputPort;
import com.seb.msusuario.application.port.in.command.CrearUbicacionCommand;
import com.seb.msusuario.infrastructure.adapter.in.web.dto.direccion.DireccionRequest;
import com.seb.msusuario.infrastructure.adapter.in.web.dto.direccion.DireccionResponse;
import com.seb.msusuario.infrastructure.adapter.in.web.mapper.DireccionWebMapper;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/usuarios/{id}/ubicaciones")
@AllArgsConstructor
public class DireccionController {
    private final DireccionInputPort direccionInputPort;
    private final DireccionWebMapper  direccionWebMapper;

    @PostMapping
    public ResponseEntity<DireccionResponse> agregarDireccion(@RequestBody @Valid DireccionRequest direccionRequest,
                                                              @PathVariable UUID id){
        CrearUbicacionCommand command = direccionWebMapper.toCommand(direccionRequest);
        DireccionResponse response = direccionWebMapper.toResponse(direccionInputPort.agregarDireccion(id,command));
        URI location = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}")
                .buildAndExpand(response.ubicacionId()).toUri();
        return ResponseEntity.created(location).body(response);
    }
    @GetMapping("/{direccionId}")
    public ResponseEntity<DireccionResponse> obtenerDireccion(@PathVariable UUID direccionId){
        DireccionResponse response = direccionWebMapper.toResponse(direccionInputPort.obtenerDireccionPorId(direccionId));
        return ResponseEntity.ok().body(response);
    }
    @PutMapping("/update/{direccionId}")
    public ResponseEntity<DireccionResponse> actualizarDireccion(@PathVariable UUID id,
                                                                 @PathVariable UUID direccionId,
                                                                 @RequestBody @Valid DireccionRequest direccionRequest){
        CrearUbicacionCommand command = direccionWebMapper.toCommand(direccionRequest);
        DireccionResponse response = direccionWebMapper.toResponse(direccionInputPort.actualizarDireccion(id,direccionId,command));
        return ResponseEntity.accepted().body(response);
    }
    @DeleteMapping("/delete/{direccionId}")
    public ResponseEntity<?> eliminarDireccion(@PathVariable UUID id,@PathVariable UUID direccionId){
        direccionInputPort.eliminarDireccion(id,direccionId);
        return ResponseEntity.noContent().build();
    }
}
