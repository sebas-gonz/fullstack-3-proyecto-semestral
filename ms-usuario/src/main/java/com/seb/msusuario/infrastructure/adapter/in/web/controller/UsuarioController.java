package com.seb.msusuario.infrastructure.adapter.in.web.controller;

import com.seb.msusuario.domain.model.Usuario;
import com.seb.msusuario.application.port.in.UsuarioInputPort;
import com.seb.msusuario.infrastructure.adapter.in.web.dto.UsuarioRequest;
import com.seb.msusuario.infrastructure.adapter.in.web.dto.UsuarioResponse;
import com.seb.msusuario.infrastructure.adapter.in.web.mapper.UsuarioDtoMapper;
import com.seb.msusuario.infrastructure.adapter.out.persistence.mapper.UsuarioMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/user")
@RequiredArgsConstructor
public class UsuarioController {
    private final UsuarioMapper usuarioMapper;
    private final UsuarioInputPort usuarioInputPort;
    private final UsuarioDtoMapper usuarioDtoMapper;

    @GetMapping("/all")
    public ResponseEntity<List<UsuarioResponse>> findAll() {
        List<UsuarioResponse> usuarioResponseList = usuarioDtoMapper.toResponseList(usuarioInputPort.obtenerTodosUsuarios());
        return ResponseEntity.status(HttpStatus.OK).body(usuarioResponseList);
    }

    @PostMapping("/create")
    public ResponseEntity<UsuarioResponse> create(@RequestBody UsuarioRequest usuarioRequest) {
        Usuario usuario = usuarioDtoMapper.toDomain(usuarioRequest);
        UsuarioResponse usuarioResponse = usuarioDtoMapper.toResponse(usuarioInputPort.crearUsuario(usuario));
        return usuarioResponse != null ? ResponseEntity.status(HttpStatus.CREATED).body(usuarioResponse) :
                ResponseEntity.status(HttpStatus.BAD_REQUEST).build();

    }
    @GetMapping("/{id}")
    public ResponseEntity<UsuarioResponse> findById(@PathVariable String id) {
        Usuario usuario = usuarioInputPort.obtenerUsuarioPorId(id);
        UsuarioResponse usuarioResponse = usuarioDtoMapper.toResponse(usuario);
        return usuarioResponse != null ? ResponseEntity.status(HttpStatus.OK).body(usuarioResponse) :
                ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
    }
    @PutMapping("/update/{id}")
    public ResponseEntity<UsuarioResponse> update(@PathVariable String id,@RequestBody UsuarioRequest usuarioRequest) {
        Usuario usuario = usuarioDtoMapper.toDomain(id,usuarioRequest);
        UsuarioResponse usuarioResponse = usuarioDtoMapper.toResponse(usuarioInputPort.actualizarUsuario(usuario));
        return usuarioResponse != null ? ResponseEntity.status(HttpStatus.OK).body(usuarioResponse) :
                ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
    }
    @DeleteMapping("/delete/{id}")
    public ResponseEntity<?> delete(@PathVariable String id){
        usuarioInputPort.elminiarUsuario(id);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }
}
