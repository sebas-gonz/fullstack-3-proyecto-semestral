package com.seb.msusuario.infrastructure.adapter.in.web.controller;

import com.seb.msusuario.application.port.in.UsuarioInputPort;
import com.seb.msusuario.application.port.in.command.CrearUsuarioCommand;
import com.seb.msusuario.infrastructure.adapter.in.web.dto.usuario.RepartidorRestResponseDto;
import com.seb.msusuario.infrastructure.adapter.in.web.dto.usuario.UsuarioRequest;
import com.seb.msusuario.infrastructure.adapter.in.web.dto.usuario.UsuarioResponse;
import com.seb.msusuario.infrastructure.adapter.in.web.mapper.UsuarioWebMapper;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;
import java.util.Set;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/usuarios")
@RequiredArgsConstructor
public class UsuarioController {
    private final UsuarioInputPort usuarioInputPort;
    private final UsuarioWebMapper usuarioWebMapper;

    @GetMapping
    public ResponseEntity<List<UsuarioResponse>> findAll() {
        List<UsuarioResponse> usuarioResponseList = usuarioWebMapper.toResponseList(usuarioInputPort.obtenerTodosUsuarios());
        return ResponseEntity.ok().body(usuarioResponseList);
    }
    @GetMapping("/{idauth0}/auth0")
    public ResponseEntity<UsuarioResponse> obtenerUsuarioPorIdAuth0(@PathVariable String idauth0){
        UsuarioResponse response = usuarioWebMapper.toResponse(usuarioInputPort.obtenerUsuarioPorIdAuth0(idauth0));
        return ResponseEntity.ok(response);
    }
    @PostMapping
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
    @PutMapping("/{id}")
    public ResponseEntity<UsuarioResponse> update(@PathVariable UUID id,@RequestBody @Valid UsuarioRequest usuarioRequest) {
        CrearUsuarioCommand  command = usuarioWebMapper.toCommand(usuarioRequest);
        UsuarioResponse usuarioResponse = usuarioWebMapper.toResponse(usuarioInputPort.actualizarUsuario(id, command));
        return ResponseEntity.accepted().body(usuarioResponse);
    }
    @DeleteMapping("/{id}")
    public ResponseEntity<?> delete(@PathVariable UUID id){
        usuarioInputPort.eliminarUsuario(id);
        return ResponseEntity.noContent().build();
    }
    @GetMapping("/repartidores")
    public ResponseEntity<Page<RepartidorRestResponseDto>> obtenerRepartidores(
            @RequestParam(defaultValue = "0") int page) {
        Pageable pageable = PageRequest.of(page, 10);
        Page<RepartidorRestResponseDto> repartidores = usuarioInputPort.obtenerRepartidores(pageable).map(usuarioWebMapper::toRepartidorResponseDto);
        return ResponseEntity.ok(repartidores);
    }

    @PostMapping("/bulk")
    public ResponseEntity<List<UsuarioResponse>> obtenerUsuariosPorIds(@RequestBody Set<UUID> usuarioIds) {
        List<UsuarioResponse> responses = usuarioWebMapper.toResponseList(usuarioInputPort.obtenerUsuariosPorIds(usuarioIds));
        return ResponseEntity.ok(responses);
    }
}
