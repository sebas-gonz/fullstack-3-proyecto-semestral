package com.seb.msbffweb.client;

import com.seb.msbffweb.dto.in.pedido.CotizacionEnvioRequestDto;
import com.seb.msbffweb.dto.out.pedido.PedidoWebResponse;
import com.seb.msbffweb.dto.out.page.PaginaRestResponse;
import com.seb.msbffweb.dto.out.pedido.CostoEnvioResponseDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Set;
import java.util.UUID;

@FeignClient(name = "ms-pedido")
public interface PedidoClient {
    @PostMapping("/api/v1/pedidos/cotizar-envio")
    CostoEnvioResponseDto cotizarEnvio(@RequestBody CotizacionEnvioRequestDto  cotizacionEnvioRequestDto);

    @GetMapping("/api/v1/pedidos/buscar-pedido")
    PaginaRestResponse<PedidoWebResponse> buscarPedido(@RequestParam(required = false, name = "parametro") String parametro,
                                             @RequestParam(defaultValue = "0", name = "page") int page,
                                             @RequestParam(defaultValue = "10", name = "size")  int size);
    @GetMapping("/api/v1/pedidos/estados")
    List<String> obtenerEstadosPedidos();

    @GetMapping("/api/v1/pedidos/estado/{estadoPedido}")
    PaginaRestResponse<PedidoWebResponse> obtenerPedidosPorEstado(@PathVariable String estadoPedido,
                                                              @RequestParam(defaultValue = "0", name = "page") int page,
                                                              @RequestParam(defaultValue = "10", name = "size")  int size);
    @GetMapping("/api/v1/pedidos/{id}")
    PedidoWebResponse obtenerPedidoPorId(@PathVariable UUID id);

    @PostMapping("/api/v1/pedidos/bulk")
    List<PedidoWebResponse> obtenerPedidosPorIds(@RequestBody Set<UUID> pedidoIds);

}
