package seb.com.msenvio.application.port.in.command.envio;

import seb.com.msenvio.application.port.in.command.ubicacion.UbicacionCommand;
import seb.com.msenvio.domain.model.Ubicacion;

import java.util.UUID;

public record CrearEnvioCommand(
        UUID pedidoId,
        UbicacionCommand origen,
        UbicacionCommand destino
) {
}
