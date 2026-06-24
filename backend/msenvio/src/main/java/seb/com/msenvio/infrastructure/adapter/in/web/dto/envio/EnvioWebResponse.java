package seb.com.msenvio.infrastructure.adapter.in.web.dto.envio;

import com.fasterxml.jackson.annotation.JsonFormat;
import seb.com.msenvio.domain.model.EstadoEnvio;
import seb.com.msenvio.infrastructure.adapter.in.web.dto.ubicacion.UbicacionWebResponse;

import java.time.Instant;
import java.util.UUID;

public record EnvioWebResponse(UUID envioId,
                               UUID pedidoId,
                               UUID repartidorId,

                               EstadoEnvio estado,
                               UbicacionWebResponse destino,
                               UbicacionWebResponse origen,
                               @JsonFormat(shape = JsonFormat.Shape.STRING)
                               Instant fechaRegistro,
                               @JsonFormat(shape = JsonFormat.Shape.STRING)
                               Instant fechaModificacion) {
}
