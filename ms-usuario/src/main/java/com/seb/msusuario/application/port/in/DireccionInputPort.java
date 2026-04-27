package com.seb.msusuario.application.port.in;

import com.seb.msusuario.infrastructure.adapter.in.web.dto.direccion.DireccionRequest;
import com.seb.msusuario.infrastructure.adapter.in.web.dto.direccion.DireccionResponse;

import java.util.List;

public interface DireccionInputPort {
    DireccionResponse agregarDireccion(String usuarioId, DireccionRequest request);
    DireccionResponse actualizarDireccion(String usuarioId,String direccionId, DireccionRequest request);
    void eliminarDireccion(String usuarioId,String direccionId);

    DireccionResponse obtenerDireccionPorId(String direccionId);
    List<DireccionResponse> obtenerTodasDirecciones();

    List<DireccionResponse> obtenerDireccionesPorUsuario(String usuarioId);
}
