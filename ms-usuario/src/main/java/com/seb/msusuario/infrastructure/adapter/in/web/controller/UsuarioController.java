package com.seb.msusuario.infrastructure.adapter.in.web.controller;

import com.seb.msusuario.application.port.in.UsuarioInputPort;
import com.seb.msusuario.infrastructure.adapter.in.web.dto.usuario.UsuarioRequest;
import com.seb.msusuario.infrastructure.adapter.in.web.dto.usuario.UsuarioResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/api/v1/user")
@RequiredArgsConstructor
public class UsuarioController {
    private final UsuarioInputPort usuarioInputPort;

    @GetMapping("/all")
    public ResponseEntity<List<UsuarioResponse>> findAll() {
        List<UsuarioResponse> usuarioResponseList = usuarioInputPort.obtenerTodosUsuarios();
        return ResponseEntity.ok().body(usuarioResponseList);
    }

    @PostMapping("/create")
    public ResponseEntity<UsuarioResponse> create(@RequestBody @Valid UsuarioRequest usuarioRequest) {
        UsuarioResponse usuarioResponse = usuarioInputPort.crearUsuario(usuarioRequest);
        URI location = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}")
                .buildAndExpand(usuarioResponse.id()).toUri();
        return ResponseEntity.created(location).body(usuarioResponse);

    }
    @GetMapping("/{id}")
    public ResponseEntity<UsuarioResponse> findById(@PathVariable String id) {
        UsuarioResponse usuario = usuarioInputPort.obtenerUsuarioPorId(id);
        return ResponseEntity.ok().body(usuario);
    }
    @PutMapping("/update/{id}")
    public ResponseEntity<UsuarioResponse> update(@PathVariable String id,@RequestBody @Valid UsuarioRequest usuarioRequest) {
        UsuarioResponse usuarioResponse = usuarioInputPort.actualizarUsuario(id, usuarioRequest);
        return ResponseEntity.accepted().body(usuarioResponse);
    }
    @DeleteMapping("/delete/{id}")
    public ResponseEntity<?> delete(@PathVariable String id){
        usuarioInputPort.eliminarUsuario(id);
        return ResponseEntity.noContent().build();
    }
}
