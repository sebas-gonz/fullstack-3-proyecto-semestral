package seb.com.msenvio.application.port.in;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import seb.com.msenvio.application.port.in.command.envio.CrearEnvioCommand;
import seb.com.msenvio.domain.model.Envio;
import seb.com.msenvio.domain.model.EstadoEnvio;

import java.util.List;
import java.util.UUID;

public interface EnvioInputPort {
    Envio crearEnvio(CrearEnvioCommand command);
    Envio asignarRepartidor(UUID envioId, UUID repartidorId);
    Envio actualizarEstado(UUID envioId, EstadoEnvio nuevoEstado);
    Envio obtenerEnvio(UUID envioId);
    Page<Envio> obtenerEnviosDisponibles(Pageable pageable);
    Page<Envio> obtenerTodosLosEnvios(Pageable pageable);
    Page<Envio> obtenerEnviosAsignados(UUID repartidorId, Pageable pageable);
    Page<Envio> obtenerEnviosEnRuta(UUID repartidorId, Pageable pageable);
    Page<Envio> obtenerEnviosEntregados(UUID repartidorId, Pageable pageable);
    List<String> obtenerEstadosDeEnvios();
    Page<Envio> obtenerEnviosPorEstado(String estado, Pageable pageable);

}
