package com.seb.msusuario.application.port.in;

import com.seb.msusuario.application.port.in.command.CrearUbicacionCommand;
import com.seb.msusuario.domain.model.Ubicacion;
import com.seb.msusuario.infrastructure.adapter.in.web.dto.direccion.DireccionRequest;
import com.seb.msusuario.infrastructure.adapter.in.web.dto.direccion.DireccionResponse;

import java.util.List;
import java.util.UUID;

public interface DireccionInputPort {
    Ubicacion agregarDireccion(UUID usuarioId, CrearUbicacionCommand command);
    Ubicacion actualizarDireccion(UUID usuarioId,UUID direccionId, CrearUbicacionCommand command);
    void eliminarDireccion(UUID usuarioId,UUID direccionId);

    Ubicacion obtenerDireccionPorId(UUID direccionId);
    List<Ubicacion> obtenerTodasDirecciones();

    List<Ubicacion> obtenerDireccionesPorUsuario(UUID usuarioId);
}
