package com.seb.msusuario.infrastructure.adapter.in.web.controller;

import com.seb.msusuario.application.port.in.DireccionInputPort;
import com.seb.msusuario.infrastructure.adapter.in.web.dto.direccion.DireccionRequest;
import com.seb.msusuario.infrastructure.adapter.in.web.dto.direccion.DireccionResponse;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;

@RestController
@RequestMapping("/api/v1/user/{id}/direccion")
@AllArgsConstructor
public class DireccionController {
    private final DireccionInputPort direccionInputPort;

    @PostMapping("/create")
    public ResponseEntity<DireccionResponse> agregarDireccion(@RequestBody @Valid DireccionRequest direccionRequest,
                                                              @PathVariable String id){
        DireccionResponse response = direccionInputPort.agregarDireccion(id,direccionRequest);
        URI location = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}")
                .buildAndExpand(response.id()).toUri();
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }
    @GetMapping("/{direccionId}")
    public ResponseEntity<DireccionResponse> obtenerDireccion(@PathVariable String direccionId){
        DireccionResponse response = direccionInputPort.obtenerDireccionPorId(direccionId);
        return ResponseEntity.ok().body(response);
    }
    @PutMapping("/update/{direccionId}")
    public ResponseEntity<DireccionResponse> actualizarDireccion(@PathVariable String id,
                                                                 @PathVariable String direccionId,
                                                                 @RequestBody @Valid DireccionRequest direccionRequest){
        DireccionResponse response = direccionInputPort.actualizarDireccion(id,direccionId,direccionRequest);
        return ResponseEntity.accepted().body(response);
    }
    @DeleteMapping("/delete/{direccionId}")
    public ResponseEntity<DireccionResponse> eliminarDireccion(@PathVariable String id,@PathVariable String direccionId){
        direccionInputPort.eliminarDireccion(id,direccionId);
        return ResponseEntity.noContent().build();
    }
}
