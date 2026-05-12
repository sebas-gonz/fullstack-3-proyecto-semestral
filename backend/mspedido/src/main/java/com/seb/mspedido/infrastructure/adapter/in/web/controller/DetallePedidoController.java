package com.seb.mspedido.infrastructure.adapter.in.web.controller;

import com.seb.mspedido.application.port.in.DetalleInputPort;
import com.seb.mspedido.infrastructure.adapter.in.web.dto.detalle.DetalleWebResponse;
import com.seb.mspedido.infrastructure.adapter.in.web.mapper.DetalleWebMapper;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/pedidos/{pedidoid}/detalles")
@AllArgsConstructor
public class DetallePedidoController {
    private final DetalleInputPort detalleInputPort;
    private final DetalleWebMapper  detalleWebMapper;

    @GetMapping
    public ResponseEntity<List<DetalleWebResponse>> obtenerDetallesPedido(@PathVariable UUID pedidoid) {
        List<DetalleWebResponse> detalleWebResponses = detalleWebMapper.toResponseList(detalleInputPort.obtenerDetallesPorPedido(pedidoid));
        return ResponseEntity.ok(detalleWebResponses);
    }
    @GetMapping("/{id}")
    public ResponseEntity<DetalleWebResponse> obtenerDetalle(@PathVariable UUID pedidoid, @PathVariable UUID id) {
        DetalleWebResponse webResponse = detalleWebMapper.toResponse(detalleInputPort.obtenerDetalle(pedidoid, id));
        return ResponseEntity.ok(webResponse);
    }
}
