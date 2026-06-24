package com.seb.mspedido.infrastructure.adapter.in.web.mapper;

import com.seb.mspedido.application.port.in.query.CotizacionEnvioQuery;
import com.seb.mspedido.domain.model.CotizacionEnvioResultado;
import com.seb.mspedido.infrastructure.adapter.in.web.dto.costoenvio.CostoEnvioWebResponseDto;
import com.seb.mspedido.infrastructure.adapter.in.web.dto.costoenvio.CotizacionEnvioWebRequestDto;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface CostoEnvioWebMapper {
    CotizacionEnvioQuery toQuery(CotizacionEnvioWebRequestDto  cotizacionEnvioWebRequestDto);
    CostoEnvioWebResponseDto toWebResponse(CotizacionEnvioResultado cotizacionEnvioResultado);
}
