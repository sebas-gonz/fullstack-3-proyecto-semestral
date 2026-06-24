package com.seb.msbffweb.controller;

import com.seb.msbffweb.client.EnvioClient;
import com.seb.msbffweb.client.PedidoClient;
import com.seb.msbffweb.client.UsuarioClient;
import com.seb.msbffweb.dto.out.envio.EnvioWebAdminPanelResponse;
import com.seb.msbffweb.dto.out.envio.EnvioWebResponse;
import com.seb.msbffweb.dto.out.page.PaginaRestResponse;
import com.seb.msbffweb.dto.out.pedido.PedidoWebResponse;
import com.seb.msbffweb.dto.out.usuario.UsuarioRestResponseDto;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.core.task.AsyncTaskExecutor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.*;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/bff/envios")
public class EnvioBffController {
    private final EnvioClient envioClient;
    private final PedidoClient pedidoClient;
    private final UsuarioClient usuarioClient;
    private final AsyncTaskExecutor applicationTaskExecutor;

    public EnvioBffController(EnvioClient envioClient,
                              PedidoClient pedidoClient,
                              UsuarioClient usuarioClient,
                              @Qualifier("applicationTaskExecutor") AsyncTaskExecutor applicationTaskExecutor) {
        this.envioClient = envioClient;
        this.pedidoClient = pedidoClient;
        this.usuarioClient = usuarioClient;
        this.applicationTaskExecutor = applicationTaskExecutor;
    }

    @GetMapping("/admin")
    public ResponseEntity<PaginaRestResponse<EnvioWebAdminPanelResponse>> obtenerTodosLosEnvios(@RequestParam(defaultValue = "0") int page, @RequestParam(defaultValue = "10") int size){
        PaginaRestResponse<EnvioWebResponse> envios = envioClient.obtenerTodosLosEnvios(page, size);
        return ResponseEntity.ok(enriquecerEnvios(envios));
    }
    @GetMapping("/estados-posibles")
    public ResponseEntity<List<String>> obtenerEstadosPosiblesDeEnvio(){
        List<String> estados = envioClient.obtenerEstadosPosiblesDeEnvios();
        return ResponseEntity.ok(estados);
    }
    @GetMapping("/estado/{estadoEnvio}")
    public ResponseEntity<PaginaRestResponse<EnvioWebAdminPanelResponse>> obtenerEnviosPorEstado(@PathVariable String estadoEnvio,
                                                                                                 @RequestParam(defaultValue = "0", name = "page") int page,
                                                                                                 @RequestParam(defaultValue = "10", name = "size")  int size){
        PaginaRestResponse<EnvioWebResponse> envios = envioClient.obtenerEnviosPorEstado(estadoEnvio,page,size);
        return ResponseEntity.ok(enriquecerEnvios(envios));
    }

    private PaginaRestResponse<EnvioWebAdminPanelResponse> enriquecerEnvios(PaginaRestResponse<EnvioWebResponse> envios) {
        if (envios.content().isEmpty()) {
            return new PaginaRestResponse<>(List.of(), envios.number(), envios.totalPages(), envios.totalElements());
        }
        Set<UUID> pedidoIds = envios.content().stream()
                .map(EnvioWebResponse::pedidoId)
                .collect(Collectors.toSet());
        List<PedidoWebResponse> pedidosBatch = pedidoClient.obtenerPedidosPorIds(pedidoIds);
        Map<UUID, PedidoWebResponse> mapaPedidos = pedidosBatch.stream()
                .collect(Collectors.toMap(PedidoWebResponse::pedidoId, p -> p));

        Set<UUID> usuarioIds = new HashSet<>();
        pedidosBatch.forEach(p -> usuarioIds.add(p.usuarioId()));
        envios.content().stream()
                .map(EnvioWebResponse::repartidorId)
                .filter(Objects::nonNull)
                .forEach(usuarioIds::add);

        List<UsuarioRestResponseDto> usuariosBatch = usuarioClient.obtenerUsuariosPorIds(usuarioIds);
        Map<UUID, UsuarioRestResponseDto> mapaUsuarios = usuariosBatch.stream()
                .collect(Collectors.toMap(UsuarioRestResponseDto::usuarioId, u -> u));

        List<EnvioWebAdminPanelResponse> contenidoEnriquecido = envios.content().stream().map(envio -> {
            PedidoWebResponse pedido = mapaPedidos.get(envio.pedidoId());
            UsuarioRestResponseDto comprador = mapaUsuarios.get(pedido.usuarioId());

            String nombreRepartidor = "Sin asignar";
            if (envio.repartidorId() != null) {
                UsuarioRestResponseDto repartidor = mapaUsuarios.get(envio.repartidorId());
                if (repartidor != null) {
                    nombreRepartidor = repartidor.nombre() + " " + repartidor.apellido();
                }
            }

            return new EnvioWebAdminPanelResponse(
                    envio.envioId(),
                    pedido.pedidoId(),
                    envio.repartidorId(),
                    comprador.usuarioId(),
                    nombreRepartidor,
                    comprador.nombre() + " " + comprador.apellido(),
                    pedido.total(),
                    envio.estado(),
                    pedido.destino(),
                    pedido.origen(),
                    envio.fechaRegistro(),
                    pedido.fechaEntrega()
            );
        }).toList();

        return new PaginaRestResponse<>(
                contenidoEnriquecido,
                envios.number(),
                envios.totalPages(),
                envios.totalElements()
        );
    }
}
