package com.seb.apigateway.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/fallback")
public class FallbackController {
    @GetMapping("/usuarios")
    public ResponseEntity<String> usuariosFallback() {
        return ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE)
                .body("El servicio de gestión de usuarios no responde. Por favor, intente más tarde.");
    }

    @GetMapping("/pedidos")
    public ResponseEntity<String> pedidosFallback() {
        return ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE)
                .body("El servicio de pedidos está experimentando problemas.");
    }

    @GetMapping("/catalogo")
    public ResponseEntity<String> catalogoFallback() {
        return ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE)
                .body("No se pudo cargar el catálogo de productos. Verifique su conexión.");
    }

    @GetMapping("/inventario")
    public ResponseEntity<String> inventarioFallback() {
        return ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE)
                .body("El servicio de inventario no está disponible. El stock no puede ser verificado en este momento.");
    }

    @GetMapping("/general")
    public ResponseEntity<String> generalFallback() {
        return ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE)
                .body("Un servicio esencial del sistema no está respondiendo. Contacte al soporte técnico.");
    }
}
