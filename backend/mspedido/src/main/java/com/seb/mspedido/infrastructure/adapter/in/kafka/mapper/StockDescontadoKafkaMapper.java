package com.seb.mspedido.infrastructure.adapter.in.kafka.mapper;

import com.seb.mspedido.application.port.in.command.stock.StockDescontadoCommand;
import com.seb.mspedido.infrastructure.adapter.in.kafka.dto.stock.StockDescontadoEventDto;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface StockDescontadoKafkaMapper {
    StockDescontadoCommand toStockDescontadoCommand(StockDescontadoEventDto evento);
}
