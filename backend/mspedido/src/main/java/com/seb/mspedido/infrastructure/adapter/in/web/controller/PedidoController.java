package com.seb.mspedido.infrastructure.adapter.in.web.controller;

import com.seb.mspedido.application.port.in.PedidoInputPort;
import com.seb.mspedido.application.port.in.command.pedido.PedidoInputCommand;
import com.seb.mspedido.application.port.in.query.CotizacionEnvioQuery;
import com.seb.mspedido.domain.model.Pedido;
import com.seb.mspedido.infrastructure.adapter.in.web.dto.costoenvio.CostoEnvioWebResponseDto;
import com.seb.mspedido.infrastructure.adapter.in.web.dto.costoenvio.CotizacionEnvioWebRequestDto;
import com.seb.mspedido.infrastructure.adapter.in.web.dto.pedido.PedidoWebRequest;
import com.seb.mspedido.infrastructure.adapter.in.web.dto.pedido.PedidoWebResponse;
import com.seb.mspedido.infrastructure.adapter.in.web.mapper.CostoEnvioWebMapper;
import com.seb.mspedido.infrastructure.adapter.in.web.mapper.PedidoWebMapper;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
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
@RequestMapping("/api/v1/pedidos")
@AllArgsConstructor
public class PedidoController {
    private final PedidoInputPort pedidoInputPort;
    private final PedidoWebMapper  pedidoWebMapper;
    private final CostoEnvioWebMapper  costoEnvioWebMapper;

    @GetMapping
    public ResponseEntity<Page<PedidoWebResponse>> listarPedidos(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
    ) {
        Pageable pageable = PageRequest.of(page,size);
        Page<Pedido> pedidosPage = pedidoInputPort.obtenerPedidos(pageable);
        Page<PedidoWebResponse> pedidoWebResponses = pedidosPage.map(pedidoWebMapper::toResponse);
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
    @GetMapping("/usuario/{id}")
    public ResponseEntity<Page<PedidoWebResponse>> obtenerPedidosPorUsuario(@PathVariable UUID id,
                                                                            @RequestParam(defaultValue = "0") int page,
                                                                            @RequestParam(defaultValue = "10") int size){
        Pageable pageable = PageRequest.of(page,size);
        Page<PedidoWebResponse> responses = pedidoInputPort.obtenerPedidosPorUsuario(id, pageable).map(pedidoWebMapper::toResponse);
        return ResponseEntity.ok(responses);
    }
    @PostMapping("/cotizar-envio")
    public ResponseEntity<CostoEnvioWebResponseDto> cotizarCostoEnvio(@Valid @RequestBody CotizacionEnvioWebRequestDto  cotizacionEnvioWebRequestDto) {
        CotizacionEnvioQuery cotizacionEnvioQuery = costoEnvioWebMapper.toQuery(cotizacionEnvioWebRequestDto);
        CostoEnvioWebResponseDto costoEnvio =costoEnvioWebMapper.toWebResponse(pedidoInputPort.cotizarCostoDeEnvio(cotizacionEnvioQuery));
         return  ResponseEntity.ok(costoEnvio);
    }
    @GetMapping("/buscar-pedido")
    public ResponseEntity<Page<PedidoWebResponse>> buscarPedido(@RequestParam(required = false) String parametro,
                                                                       @RequestParam(defaultValue = "0") int page,
                                                                       @RequestParam(defaultValue = "10")  int size) {
        Pageable pageable = PageRequest.of(page,size);
        Page<PedidoWebResponse> pedidosResponse = pedidoInputPort.buscarPedido(parametro,pageable).map(pedidoWebMapper::toResponse);
        return ResponseEntity.ok(pedidosResponse);
    }
    @GetMapping("/estados")
    public ResponseEntity<List<String>> obtenerEstadosPedidos(){
        return ResponseEntity.ok().body(pedidoInputPort.obtenerEstadosPedidos());
    }
    @GetMapping("/estado/{estadoPedido}")
    public ResponseEntity<Page<PedidoWebResponse>> obtenerPedidosPorEstado(@PathVariable String estadoPedido,
                                                                           @RequestParam(defaultValue = "0") int page,
                                                                           @RequestParam(defaultValue = "10")  int size){
        Pageable pageable = PageRequest.of(page,size);
        Page<PedidoWebResponse> pedidosResponse = pedidoInputPort.obtenerPedidosPorEstado(estadoPedido,pageable).map(pedidoWebMapper::toResponse);
        return ResponseEntity.ok(pedidosResponse);
    }
    @PostMapping("/bulk")
    public ResponseEntity<List<PedidoWebResponse>> obtenerPedidosPorIds(@RequestBody Set<UUID> pedidoIds) {
        List<PedidoWebResponse> responses = pedidoWebMapper.toResponseList(pedidoInputPort.obtenerPedidosPorIds(pedidoIds));
        return ResponseEntity.ok(responses);
    }
}
