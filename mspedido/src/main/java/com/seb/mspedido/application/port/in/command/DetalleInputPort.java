package com.seb.mspedido.application.port.in.command;

import com.seb.mspedido.application.port.in.command.detalle.DetalleInputCommand;
import com.seb.mspedido.domain.model.Detalle;

import java.util.List;
import java.util.UUID;

public interface DetalleInputPort {
    Detalle guardarDetalle(UUID pedidoId, DetalleInputCommand detalleInputCommand);
    Detalle obtenerDetalle(UUID pedidoId,  UUID detalleId);
    List<Detalle> obtenerDetalles(UUID pedidoId);
    void eliminarDetalle(UUID pedidoId, UUID detalleId);
}
