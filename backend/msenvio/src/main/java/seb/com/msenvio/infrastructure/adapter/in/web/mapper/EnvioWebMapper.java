package seb.com.msenvio.infrastructure.adapter.in.web.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;
import seb.com.msenvio.application.port.in.command.envio.CrearEnvioCommand;
import seb.com.msenvio.application.port.in.command.ubicacion.UbicacionCommand;
import seb.com.msenvio.domain.model.Envio;
import seb.com.msenvio.domain.model.Ubicacion;
import seb.com.msenvio.infrastructure.adapter.in.web.dto.envio.EnvioWebRequest;
import seb.com.msenvio.infrastructure.adapter.in.web.dto.envio.EnvioWebResponse;
import seb.com.msenvio.infrastructure.adapter.in.web.dto.ubicacion.UbicacionWebRequest;
import seb.com.msenvio.infrastructure.adapter.in.web.dto.ubicacion.UbicacionWebResponse;

import java.util.List;
@Mapper(componentModel = MappingConstants.ComponentModel.SPRING )
public interface EnvioWebMapper {
    CrearEnvioCommand toCommand(EnvioWebRequest envioWebRequest);
    UbicacionCommand toCommand(UbicacionWebRequest ubicacionWebRequest);
    EnvioWebResponse toResponse(Envio Envio);
    UbicacionWebResponse toResponse(Ubicacion ubicacion);
    List<EnvioWebResponse> toResponseList(List<Envio> Envios);
}
