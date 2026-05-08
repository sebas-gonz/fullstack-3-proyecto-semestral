package com.seb.msusuario.infrastructure.adapter.in.web.controller;

import com.seb.msusuario.application.port.in.UsuarioInputPort;
import com.seb.msusuario.application.port.in.command.CrearUsuarioCommand;
import com.seb.msusuario.infrastructure.adapter.in.web.dto.usuario.UsuarioRequest;
import com.seb.msusuario.infrastructure.adapter.in.web.dto.usuario.UsuarioResponse;
import com.seb.msusuario.infrastructure.adapter.in.web.mapper.UsuarioWebMapper;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/user")
@RequiredArgsConstructor
public class UsuarioController {
    private final UsuarioInputPort usuarioInputPort;
    private final UsuarioWebMapper usuarioWebMapper;

    @GetMapping("/all")
    public ResponseEntity<List<UsuarioResponse>> findAll() {
        List<UsuarioResponse> usuarioResponseList = usuarioWebMapper.toResponseList(usuarioInputPort.obtenerTodosUsuarios());
        return ResponseEntity.ok().body(usuarioResponseList);
    }

    @PostMapping("/create")
    public ResponseEntity<UsuarioResponse> create(@RequestBody @Valid UsuarioRequest usuarioRequest) {
        CrearUsuarioCommand  command = usuarioWebMapper.toCommand(usuarioRequest);
        UsuarioResponse usuarioResponse = usuarioWebMapper.toResponse(usuarioInputPort.crearUsuario(command));
        URI location = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}")
                .buildAndExpand(usuarioResponse.usuarioId()).toUri();
        return ResponseEntity.created(location).body(usuarioResponse);

    }
    @GetMapping("/{id}")
    public ResponseEntity<UsuarioResponse> findById(@PathVariable UUID id) {
        UsuarioResponse usuario = usuarioWebMapper.toResponse(usuarioInputPort.obtenerUsuarioPorId(id));
        return ResponseEntity.ok().body(usuario);
    }
    @PutMapping("/update/{id}")
    public ResponseEntity<UsuarioResponse> update(@PathVariable UUID id,@RequestBody @Valid UsuarioRequest usuarioRequest) {
        CrearUsuarioCommand  command = usuarioWebMapper.toCommand(usuarioRequest);
        UsuarioResponse usuarioResponse = usuarioWebMapper.toResponse(usuarioInputPort.actualizarUsuario(id, command));
        return ResponseEntity.accepted().body(usuarioResponse);
    }
    @DeleteMapping("/delete/{id}")
    public ResponseEntity<?> delete(@PathVariable UUID id){
        usuarioInputPort.eliminarUsuario(id);
        return ResponseEntity.noContent().build();
    }
}
