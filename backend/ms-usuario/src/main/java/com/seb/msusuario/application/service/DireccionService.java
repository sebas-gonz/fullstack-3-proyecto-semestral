package com.seb.msusuario.application.service;

import com.seb.msusuario.application.exception.AddressNotFoundException;
import com.seb.msusuario.application.exception.UserNotFoundException;
import com.seb.msusuario.application.port.in.DireccionInputPort;
import com.seb.msusuario.application.port.in.command.CrearUbicacionCommand;
import com.seb.msusuario.application.port.out.DireccionOutputPort;
import com.seb.msusuario.application.port.out.GeocodingOutPutPort;
import com.seb.msusuario.application.port.out.UsuarioOutputPort;
import com.seb.msusuario.domain.model.Coordenadas;
import com.seb.msusuario.domain.model.Ubicacion;
import com.seb.msusuario.domain.model.Usuario;
import com.seb.msusuario.infrastructure.adapter.in.web.dto.direccion.DireccionRequest;
import com.seb.msusuario.infrastructure.adapter.in.web.dto.direccion.DireccionResponse;
import com.seb.msusuario.infrastructure.adapter.in.web.mapper.DireccionWebMapper;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class DireccionService implements DireccionInputPort {
    private final GeocodingOutPutPort geocodingOutPutPort;
    private final UsuarioOutputPort usuarioOutputPort;
    private final DireccionWebMapper direccionWebMapper;
    private final DireccionOutputPort direccionOutputPort;
    @Override
    public Ubicacion agregarDireccion(UUID usuarioId, CrearUbicacionCommand  command) {
        Usuario usuario = usuarioOutputPort.obtenerUsuarioPorId(usuarioId)
                .orElseThrow(() -> new UserNotFoundException(usuarioId));
        String lugar = String.format("%s %s %s %s", command.calle(),command.numero(),command.ciudad(),command.pais());
        Coordenadas coordenadas = geocodingOutPutPort.obtenerCoordenadas(lugar);
        Ubicacion ubicacion = Ubicacion.builder()
                .calle(command.calle())
                .numero(command.numero())
                .ciudad(command.ciudad())
                .pais(command.pais())
                .latitude(coordenadas.getLatitude())
                .longitude(coordenadas.getLongitude())
                .usuarioId(usuarioId)
                .build();

        usuario.getDirecciones().add(ubicacion);
        return direccionOutputPort.guardarUbicacion(ubicacion);
    }
    @Override
    @Transactional
    public Ubicacion actualizarDireccion(UUID usuarioId, UUID direccionId, CrearUbicacionCommand command) {
        Usuario usuario = usuarioOutputPort.obtenerUsuarioPorId(usuarioId).orElseThrow(
                () -> new UserNotFoundException(usuarioId)
        );
        List<Ubicacion> direccionesUsuario = usuario.getDirecciones();
        Ubicacion ubicacion = direccionesUsuario.stream()
                .filter(d -> d.getUbicacionId().equals(direccionId)).findFirst()
                .orElseThrow(() -> new AddressNotFoundException(direccionId));
        String lugar = String.format("%s %s %s %s", command.calle(), command.numero(), command.ciudad(), command.pais());
        Coordenadas coordenadas = geocodingOutPutPort.obtenerCoordenadas(lugar);
        ubicacion.setCalle(command.calle());
        ubicacion.setNumero(command.numero());
        ubicacion.setCiudad(command.ciudad());
        ubicacion.setPais(command.pais());
        ubicacion.setLatitude(coordenadas.getLatitude());
        ubicacion.setLongitude(coordenadas.getLongitude());
        usuarioOutputPort.guardarUsuario(usuario);
        return usuarioOutputPort.guardarUsuario(usuario).getDirecciones().stream()
                .filter(u -> u.getUbicacionId().equals(direccionId)).findFirst()
                .orElseThrow(() -> new AddressNotFoundException(direccionId));
    }
    @Override
    public void eliminarDireccion(UUID usuarioId, UUID direccionId) {
        Usuario usuario =  usuarioOutputPort.obtenerUsuarioPorId(usuarioId).orElseThrow(
                () -> new UserNotFoundException(usuarioId)
        );
        List<Ubicacion> direccionesUsuario = usuario.getDirecciones();
        boolean eliminado = direccionesUsuario.removeIf(d -> d.getUbicacionId().equals(direccionId));
        if (!eliminado) {
            throw new AddressNotFoundException(direccionId);
        }
        usuarioOutputPort.guardarUsuario(usuario);
    }

    @Override
    public Ubicacion obtenerDireccionPorId(UUID direccionId) {
        return direccionOutputPort.obtenerDireccionPorId(direccionId)
                .orElseThrow(() -> new AddressNotFoundException(direccionId));
    }

    @Override
    public List<Ubicacion> obtenerTodasDirecciones() {
        return direccionOutputPort.obtenerTodasDirecciones();
    }


    @Override
    public List<Ubicacion> obtenerDireccionesPorUsuario(UUID usuarioId) {
        Usuario usuario = usuarioOutputPort.obtenerUsuarioPorId(usuarioId).orElseThrow(
                () -> new UserNotFoundException(usuarioId)
        );
        return usuario.getDirecciones();
    }

}
