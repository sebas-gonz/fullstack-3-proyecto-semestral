package seb.com.msenvio.application.port.in;
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
    List<Envio> obtenerEnviosDisponibles();
    List<Envio> obtenerTodosLosEnvios();

}
