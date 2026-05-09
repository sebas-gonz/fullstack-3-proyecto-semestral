package com.seb.msinventario.infrastructure.adapter.in.web.mapper;

import com.seb.msinventario.application.port.in.command.stock.StockInputCommand;
import com.seb.msinventario.domain.model.Stock;
import com.seb.msinventario.infrastructure.adapter.in.web.dto.inventario.InventarioWebRequest;
import com.seb.msinventario.infrastructure.adapter.in.web.dto.stock.StockWebRequest;
import com.seb.msinventario.infrastructure.adapter.in.web.dto.stock.StockWebResponse;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;

import java.util.List;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface StockWebMapper {
    StockInputCommand toCommand(StockWebRequest request);
    StockWebResponse toResponse(Stock stock);

    List<StockWebResponse> toResponseList(List<Stock> stocks);
}
