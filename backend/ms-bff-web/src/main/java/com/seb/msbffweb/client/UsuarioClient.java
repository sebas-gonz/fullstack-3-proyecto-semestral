package com.seb.msbffweb.client;

import com.seb.msbffweb.dto.out.page.PaginaRestResponse;
import com.seb.msbffweb.dto.out.usuario.RepartidorRestResponseDto;
import com.seb.msbffweb.dto.out.usuario.UsuarioRestResponseDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Set;
import java.util.UUID;

@FeignClient(name = "ms-usuario")
public interface UsuarioClient {
    @GetMapping("/api/v1/usuarios/repartidores")
    PaginaRestResponse<RepartidorRestResponseDto> obtenerRepartidores(@RequestParam(name = "page") int page);

    @GetMapping("/api/v1/usuarios/{id}")
    UsuarioRestResponseDto obtenerUsuario(@PathVariable("id") UUID id);

    @PostMapping("/api/v1/usuarios/bulk")
    List<UsuarioRestResponseDto> obtenerUsuariosPorIds(@RequestBody Set<UUID> usuarioIds);
}
