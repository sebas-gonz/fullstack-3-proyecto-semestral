package com.seb.mspedido.infrastructure.adapter.in.web.controller;

import com.seb.mspedido.application.port.in.PedidoInputPort;
import com.seb.mspedido.application.port.in.command.pedido.PedidoInputCommand;
import com.seb.mspedido.domain.model.Pedido;
import com.seb.mspedido.infrastructure.adapter.in.web.dto.pedido.PedidoWebRequest;
import com.seb.mspedido.infrastructure.adapter.in.web.dto.pedido.PedidoWebResponse;
import com.seb.mspedido.infrastructure.adapter.in.web.mapper.PedidoWebMapper;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/pedidos")
@AllArgsConstructor
public class PedidoController {
    private final PedidoInputPort pedidoInputPort;
    private final PedidoWebMapper  pedidoWebMapper;

    @GetMapping
    public ResponseEntity<List<PedidoWebResponse>> listarPedidos() {
        List<Pedido> pedidos = pedidoInputPort.obtenerPedidos();
        List<PedidoWebResponse> pedidoWebResponses = pedidoWebMapper.toResponseList(pedidos);
        return ResponseEntity.ok(pedidoWebResponses);
    }
    @GetMapping("/{id}")
    public ResponseEntity<PedidoWebResponse> obtenerPedidoPorId(@PathVariable UUID id) {
        Pedido pedido = pedidoInputPort.obtenerPedido(id);
        PedidoWebResponse pedidoWebResponse = pedidoWebMapper.toResponse(pedido);
        return ResponseEntity.ok(pedidoWebResponse);
    }
    @PostMapping
    public ResponseEntity<PedidoWebResponse> crearPedido(@Valid @RequestBody PedidoWebRequest pedidoWebRequest) {
        PedidoInputCommand pedidoInputCommand= pedidoWebMapper.toCommand(pedidoWebRequest);
        PedidoWebResponse pedidoWebResponse = pedidoWebMapper.toResponse(pedidoInputPort.guardarPedido(pedidoInputCommand));
        URI location = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}").buildAndExpand(pedidoWebResponse.pedidoId()).toUri();
        return ResponseEntity.created(location).body(pedidoWebResponse);
    }
    @PutMapping("/{id}")
    public ResponseEntity<PedidoWebResponse>  actualizarPedido(@PathVariable UUID id,@Valid @RequestBody PedidoWebRequest pedidoWebRequest) {
        PedidoInputCommand  pedidoInputCommand= pedidoWebMapper.toCommand(pedidoWebRequest);
        PedidoWebResponse pedidoWebResponse = pedidoWebMapper.toResponse(pedidoInputPort.actualizarPedido(id,pedidoInputCommand));
        return ResponseEntity.ok(pedidoWebResponse);
    }
    @DeleteMapping("/{id}")
    public ResponseEntity<?> eliminarPedido(@PathVariable UUID id) {
        pedidoInputPort.eliminarPedido(id);
        return ResponseEntity.noContent().build();
    }
}
