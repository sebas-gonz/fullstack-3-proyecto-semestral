package seb.com.msenvio.infrastructure.adapter.in.web.controller;

import lombok.AllArgsConstructor;
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
    public ResponseEntity<List<EnvioWebResponse>> getAllEnviosDisponibles() {
        List<EnvioWebResponse> responses = envioWebMapper.toResponseList(envioInputPort.obtenerEnviosDisponibles());
        return ResponseEntity.ok(responses);
    }
    @GetMapping
    public ResponseEntity<List<EnvioWebResponse>> getAllEnvios() {
        List<EnvioWebResponse> envios = envioWebMapper.toResponseList(envioInputPort.obtenerTodosLosEnvios());
        return ResponseEntity.ok(envios);
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
    public ResponseEntity<EnvioWebResponse> asignarRepartidor(@PathVariable UUID id) {
        Envio envio = envioInputPort.actualizarEstado(id, EstadoEnvio.ENTREGADO);
        return ResponseEntity.ok(envioWebMapper.toResponse(envio));
    }

}
