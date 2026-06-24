package seb.com.msenvio.infrastructure.adapter.in.web.controller;

import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import seb.com.msenvio.application.port.in.EnvioInputPort;
import seb.com.msenvio.domain.model.Envio;
import seb.com.msenvio.domain.model.EstadoEnvio;
import seb.com.msenvio.infrastructure.adapter.in.web.dto.envio.EnvioWebResponse;
import seb.com.msenvio.infrastructure.adapter.in.web.mapper.EnvioWebMapper;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/envios")
@AllArgsConstructor
public class EnvioController {
    private final EnvioInputPort  envioInputPort;
    private final EnvioWebMapper  envioWebMapper;

    @GetMapping("/disponibles")
    public ResponseEntity<Page<EnvioWebResponse>> obtenerTodosLosEnviosDisponibles(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
    ) {
        Pageable pageable = PageRequest.of(page, size);
        Page<Envio> envioPage = envioInputPort.obtenerEnviosDisponibles(pageable);
        Page<EnvioWebResponse> responses = envioPage.map(envioWebMapper::toResponse);
        return ResponseEntity.ok(responses);
    }
    @GetMapping("/{repartidorid}/asignados")
    public ResponseEntity<Page<EnvioWebResponse>> obtenerEnviosAsignados(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @PathVariable UUID repartidorid
    ) {
      Pageable pageable = PageRequest.of(page, size);
      Page<Envio> envioPage = envioInputPort.obtenerEnviosAsignados(repartidorid, pageable);
      Page<EnvioWebResponse> responses = envioPage.map(envioWebMapper::toResponse);
      return ResponseEntity.ok(responses);
    }
    @GetMapping("/{repartidorid}/enruta")
    public ResponseEntity<Page<EnvioWebResponse>> obtenerEnviosEnruta(
            @PathVariable UUID repartidorid,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
    ){
        Pageable pageable = PageRequest.of(page, size);
        Page<Envio> envioPage = envioInputPort.obtenerEnviosEnRuta(repartidorid, pageable);
        Page<EnvioWebResponse>  responses = envioPage.map(envioWebMapper::toResponse);
        return ResponseEntity.ok(responses);
    }
    @GetMapping("/{repartidorid}/entregado")
    public ResponseEntity<Page<EnvioWebResponse>> obtenerEnviosEntregados(
            @PathVariable UUID repartidorid,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
    ){
        Pageable pageable = PageRequest.of(page, size);
        Page<Envio> envioPage = envioInputPort.obtenerEnviosEntregados(repartidorid, pageable);
        Page<EnvioWebResponse> responses = envioPage.map(envioWebMapper::toResponse);
        return ResponseEntity.ok(responses);
    }
    @GetMapping
    public ResponseEntity<Page<EnvioWebResponse>> obtenerTodosLosEnvios(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
    ) {
        Pageable pageable = PageRequest.of(page, size);
        Page<Envio> envioPage = envioInputPort.obtenerTodosLosEnvios(pageable);
        Page<EnvioWebResponse> responses = envioPage.map(envioWebMapper::toResponse);
        return ResponseEntity.ok(responses);
    }
    @GetMapping("/{id}")
    public ResponseEntity<EnvioWebResponse> getEnvioById(@PathVariable UUID id) {
        EnvioWebResponse envioWebResponse = envioWebMapper.toResponse(envioInputPort.obtenerEnvio(id));
        return ResponseEntity.ok(envioWebResponse);
    }

    @PostMapping("/{id}/asignar")
    public ResponseEntity<EnvioWebResponse> asignarRepartidor(@PathVariable UUID id, @RequestParam UUID repartidorId) {
        Envio envio = envioInputPort.asignarRepartidor(id, repartidorId);
        envio = envioInputPort.actualizarEstado(envio.getEnvioId(), EstadoEnvio.EN_RUTA);
        return ResponseEntity.ok(envioWebMapper.toResponse(envio));
    }
    @PostMapping("/{id}/entregar")
    public ResponseEntity<EnvioWebResponse> entregarPedido(@PathVariable UUID id) {
        Envio envio = envioInputPort.actualizarEstado(id, EstadoEnvio.ENTREGADO);
        return ResponseEntity.ok(envioWebMapper.toResponse(envio));
    }

    @GetMapping("/estado-envios")
    public ResponseEntity<List<String>> obtenerEstadoDeEnvios() {
        List<String> estadoDeEnvios = envioInputPort.obtenerEstadosDeEnvios();
        return ResponseEntity.ok(estadoDeEnvios);
    }
    @GetMapping("/estado/{estado}")
    public ResponseEntity<Page<EnvioWebResponse>> obtenerEnviosPorEstado(@PathVariable String estado,
                                                                         @RequestParam(defaultValue = "0") int page,
                                                                         @RequestParam(defaultValue = "10") int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<EnvioWebResponse> enviosResponse = envioInputPort.obtenerEnviosPorEstado(estado,pageable).map(envioWebMapper::toResponse);
        return ResponseEntity.ok(enviosResponse);
    }
}
