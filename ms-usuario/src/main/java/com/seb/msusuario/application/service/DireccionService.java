package com.seb.msusuario.application.service;

import com.seb.msusuario.application.exception.AddressNotFoundException;
import com.seb.msusuario.application.exception.UserNotFoundException;
import com.seb.msusuario.application.port.in.DireccionInputPort;
import com.seb.msusuario.application.port.out.DireccionOutputPort;
import com.seb.msusuario.application.port.out.GeocodingOutPutPort;
import com.seb.msusuario.application.port.out.UsuarioOutputPort;
import com.seb.msusuario.domain.model.Coordenadas;
import com.seb.msusuario.domain.model.Direccion;
import com.seb.msusuario.domain.model.Usuario;
import com.seb.msusuario.infrastructure.adapter.in.web.dto.direccion.DireccionRequest;
import com.seb.msusuario.infrastructure.adapter.in.web.dto.direccion.DireccionResponse;
import com.seb.msusuario.infrastructure.adapter.in.web.mapper.DireccionDtoMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class DireccionService implements DireccionInputPort {
    private final GeocodingOutPutPort geocodingOutPutPort;
    private final UsuarioOutputPort usuarioOutputPort;
    private final DireccionDtoMapper  direccionDtoMapper;
    private final DireccionOutputPort direccionOutputPort;
    @Override
    public DireccionResponse agregarDireccion(String usuarioId, DireccionRequest request) {
        Optional<Usuario> usuarioOptional = usuarioOutputPort.obtenerUsuarioPorId(usuarioId);
        if (usuarioOptional.isEmpty()) {
            throw new UserNotFoundException(usuarioId);
        }
        String lugar = String.format("%s %s %s %s", request.calle(),request.numero(),request.ciudad(),request.pais());
        Coordenadas coordenadas = geocodingOutPutPort.obtenerCoordenadas(lugar);
        Direccion direccion = Direccion.builder()
                                .calle(request.calle())
                                .numero(request.numero())
                                .ciudad(request.ciudad())
                                .pais(request.pais())
                                .latitude(coordenadas.getLatitude())
                                .longitude(coordenadas.getLongitude())
                                .build();
        usuarioOptional.get().getDirecciones().add(direccion);
        usuarioOutputPort.guardarUsuario(usuarioOptional.get());
        return direccionDtoMapper.toResponse(direccion);
    }
    @Override
    public DireccionResponse actualizarDireccion(String usuarioId, String direccionId, DireccionRequest request) {
        Optional<Usuario> usuarioOptional = usuarioOutputPort.obtenerUsuarioPorId(usuarioId);
        if (usuarioOptional.isEmpty()) {
            throw new UserNotFoundException(usuarioId);
        }
        List<Direccion> direccionesUsuario = usuarioOptional.get().getDirecciones();
        Direccion direccion = direccionesUsuario.stream().filter(d -> d.getId().equals(direccionId)).findFirst()
                .orElseThrow(() -> new AddressNotFoundException(direccionId));
        String lugar = String.format("%s %s %s %s", request.calle(),request.numero(),request.ciudad(),request.pais());
        Coordenadas coordenadas = geocodingOutPutPort.obtenerCoordenadas(lugar);
        direccion.setCalle(request.calle());
        direccion.setNumero(request.numero());
        direccion.setCiudad(request.ciudad());
        direccion.setPais(request.pais());
        direccion.setLatitude(coordenadas.getLatitude());
        direccion.setLongitude(coordenadas.getLongitude());
        usuarioOutputPort.guardarUsuario(usuarioOptional.get());
        return direccionDtoMapper.toResponse(direccion);
    }

    @Override
    public void eliminarDireccion(String usuarioId, String direccionId) {
        Optional<Usuario> usuarioOptional = usuarioOutputPort.obtenerUsuarioPorId(usuarioId);
        if (usuarioOptional.isEmpty()) {
            throw new UserNotFoundException(usuarioId);
        }
        List<Direccion> direccionesUsuario = usuarioOptional.get().getDirecciones();
        direccionesUsuario.removeIf(d -> d.getId().equals(direccionId));
        usuarioOutputPort.guardarUsuario(usuarioOptional.get());
    }

    @Override
    public DireccionResponse obtenerDireccionPorId(String direccionId) {
        Optional<Direccion> direccionOptional = direccionOutputPort.obtenerDireccionPorId(direccionId);
        if (direccionOptional.isEmpty()) {
            throw new AddressNotFoundException(direccionId);
        }
        return direccionDtoMapper.toResponse(direccionOptional.get());
    }

    @Override
    public List<DireccionResponse> obtenerTodasDirecciones() {
        List<Direccion> direcciones =  direccionOutputPort.obtenerTodasDirecciones();
        return direccionDtoMapper.toResponseList(direcciones);
    }


    @Override
    public List<DireccionResponse> obtenerDireccionesPorUsuario(String usuarioId) {
        Optional<Usuario> usuarioOptional = usuarioOutputPort.obtenerUsuarioPorId(usuarioId);
        if (usuarioOptional.isEmpty()) {
            throw new UserNotFoundException(usuarioId);
        }
        List<Direccion> direccionesUsuario = usuarioOptional.get().getDirecciones();
        return direccionDtoMapper.toResponseList(direccionesUsuario);
    }

}
