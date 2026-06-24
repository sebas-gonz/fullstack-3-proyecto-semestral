package com.seb.msbffweb.controller;

import com.seb.msbffweb.client.UsuarioClient;
import com.seb.msbffweb.dto.out.page.PaginaRestResponse;
import com.seb.msbffweb.dto.out.usuario.RepartidorRestResponseDto;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/bff/usuarios")
@AllArgsConstructor
public class UsuarioBffController {
    private final UsuarioClient  usuarioClient;

    @GetMapping("/repartidores")
    public ResponseEntity<PaginaRestResponse<RepartidorRestResponseDto>> obtenerRepartidores(@RequestParam int page){
        PaginaRestResponse<RepartidorRestResponseDto> repartidores = usuarioClient.obtenerRepartidores(page);
        return ResponseEntity.ok(repartidores);
    }
}
