package seb.com.msenvio.infrastructure.adapter.in.web.dto.envio;

import jakarta.validation.Valid;
import seb.com.msenvio.infrastructure.adapter.in.web.dto.ubicacion.UbicacionWebRequest;

import java.util.UUID;

public record EnvioWebRequest(UUID pedidoId,
                              @Valid UbicacionWebRequest origen,
                              @Valid UbicacionWebRequest destino) {
}
