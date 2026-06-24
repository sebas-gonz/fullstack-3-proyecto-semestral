package com.seb.msbffweb.controller;

import com.seb.msbffweb.client.PedidoClient;
import com.seb.msbffweb.dto.in.pedido.CotizacionEnvioRequestDto;
import com.seb.msbffweb.dto.out.pedido.PedidoWebResponse;
import com.seb.msbffweb.dto.out.page.PaginaRestResponse;
import com.seb.msbffweb.dto.out.pedido.CostoEnvioResponseDto;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/bff/pedidos")
@AllArgsConstructor
public class PedidoBffController {
    private final PedidoClient pedidoClient;

    @PostMapping("/cotizar-envio")
    public ResponseEntity<CostoEnvioResponseDto> cotizarEnvio(@Valid @RequestBody CotizacionEnvioRequestDto  cotizacionEnvioRequestDto) {
        CostoEnvioResponseDto costoEnvioResponseDto = pedidoClient.cotizarEnvio(cotizacionEnvioRequestDto);
        return ResponseEntity.ok().body(costoEnvioResponseDto);
    }
    @GetMapping("/buscar-pedido")
    public ResponseEntity<PaginaRestResponse<PedidoWebResponse>> buscarPedido(@RequestParam(required = false, name = "parametro") String parametro,
                                                                                           @RequestParam(defaultValue = "0", name = "page") int page,
                                                                                           @RequestParam(defaultValue = "10", name = "size")  int size){
        PaginaRestResponse<PedidoWebResponse> pedidos = pedidoClient.buscarPedido(parametro,page,size);
        return ResponseEntity.ok().body(pedidos);
    }
    @GetMapping("estados-pedidos")
    public ResponseEntity<List<String>> estadosPedidos(){
        List<String> estadosDeEnvios = pedidoClient.obtenerEstadosPedidos();
        return ResponseEntity.ok().body(estadosDeEnvios);
    }
    @GetMapping("/estado/{estadoPedido}")
    public ResponseEntity<PaginaRestResponse<PedidoWebResponse>> buscarPedidosPorEstado(@PathVariable String estadoPedido,
                                                                                    @RequestParam(defaultValue = "0", name = "page") int page,
                                                                                    @RequestParam(defaultValue = "10", name = "size")  int size){
        PaginaRestResponse<PedidoWebResponse> pedidos = pedidoClient.obtenerPedidosPorEstado(estadoPedido,page,size);
        return ResponseEntity.ok().body(pedidos);
    }
}
