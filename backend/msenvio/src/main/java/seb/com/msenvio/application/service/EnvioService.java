package seb.com.msenvio.application.service;

import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import seb.com.msenvio.application.mapper.EnvioDomainMapper;
import seb.com.msenvio.application.port.in.EnvioInputPort;
import seb.com.msenvio.application.port.in.command.envio.CrearEnvioCommand;
import seb.com.msenvio.application.port.out.EnvioEventPublisherPort;
import seb.com.msenvio.application.port.out.EnvioOutputPort;
import seb.com.msenvio.domain.model.Envio;
import seb.com.msenvio.domain.model.EstadoEnvio;

import java.util.UUID;

@Service
@AllArgsConstructor
@Slf4j
public class EnvioService implements EnvioInputPort {
    private final EnvioOutputPort envioOutputPort;
    private final EnvioDomainMapper envioDomainMapper;
    private final EnvioEventPublisherPort  envioEventPublisherPort;
    @Override
    public Envio crearEnvio(CrearEnvioCommand command) {
        Envio envio = envioDomainMapper.toDomain(command);
        envio.setEstado(EstadoEnvio.DISPONIBLE);
        return envioOutputPort.guardarEnvio(envio);
    }

    @Override
    @Transactional
    public Envio asignarRepartidor(UUID envioId, UUID repartidorId) {
        Envio envio = envioOutputPort.obtenerEnvioPorId(envioId)
                .orElseThrow(() ->  new RuntimeException("envio no encontrado"));
        envio.setRepartidorId(repartidorId);
        return envioOutputPort.guardarEnvio(envio);
    }

    @Override
    @Transactional
    public Envio actualizarEstado(UUID envioId, EstadoEnvio nuevoEstado) {
        Envio envio = envioOutputPort.obtenerEnvioPorId(envioId)
                .orElseThrow(() ->  new RuntimeException("envio no encontrado"));
        envio.setEstado(nuevoEstado);
        Envio guardado = envioOutputPort.guardarEnvio(envio);
        if (guardado.getEstado().equals(EstadoEnvio.EN_RUTA)) {
            envioEventPublisherPort.publicarEnvioEnRuta(guardado.getPedidoId());
        } else if (guardado.getEstado().equals(EstadoEnvio.ENTREGADO)) {
            envioEventPublisherPort.publicarEnvioEntregado(guardado.getPedidoId());
        }
        return guardado;
    }

    @Override
    public Envio obtenerEnvio(UUID envioId) {
        return envioOutputPort.obtenerEnvioPorId(envioId).orElseThrow(() ->  new RuntimeException("envio no encontrado"));
    }

    @Override
    public Page<Envio> obtenerEnviosDisponibles(Pageable pageable) {
        return envioOutputPort.obtenerEnviosPorEstado(EstadoEnvio.DISPONIBLE, pageable);
    }

    @Override
    public Page<Envio> obtenerTodosLosEnvios(Pageable pageable) {
        return envioOutputPort.obtenerEnvios(pageable);
    }

    @Override
    public Page<Envio> obtenerEnviosAsignados(UUID repartidorId, Pageable pageable) {
        return envioOutputPort.obtenerEnviosPorRepartidor(repartidorId, pageable);
    }

    @Override
    public Page<Envio> obtenerEnviosEnRuta(UUID repartidorId, Pageable pageable) {
        return envioOutputPort.obtenerEnviosPorEstadoYRepartidor(EstadoEnvio.EN_RUTA, repartidorId, pageable);
    }

    @Override
    public Page<Envio> obtenerEnviosEntregados(UUID repartidorId, Pageable pageable) {
        return envioOutputPort.obtenerEnviosPorEstadoYRepartidor(EstadoEnvio.ENTREGADO, repartidorId, pageable);
    }
}
