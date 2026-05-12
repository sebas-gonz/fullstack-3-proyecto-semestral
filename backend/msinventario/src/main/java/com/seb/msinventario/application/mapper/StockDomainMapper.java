package com.seb.msinventario.application.mapper;

import com.seb.msinventario.application.port.in.command.stock.StockInputCommand;
import com.seb.msinventario.domain.model.Stock;
import com.seb.msinventario.infrastructure.adapter.in.web.dto.stock.StockWebRequest;
import com.seb.msinventario.infrastructure.adapter.in.web.mapper.StockWebMapper;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface StockDomainMapper {
    Stock toDomain(StockInputCommand stockInputCommand);
}
