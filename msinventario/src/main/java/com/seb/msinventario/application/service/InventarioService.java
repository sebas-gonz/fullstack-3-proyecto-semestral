package com.seb.msinventario.application.service;

import com.seb.msinventario.application.exception.InventoryNotFoundException;
import com.seb.msinventario.application.port.in.InventarioInputPort;
import com.seb.msinventario.application.port.in.command.inventario.InventarioInputCommand;
import com.seb.msinventario.application.port.in.command.mapper.InventarioCommandMapper;
import com.seb.msinventario.application.port.out.InventarioOutputPort;
import com.seb.msinventario.domain.model.Inventario;
import com.seb.msinventario.domain.model.Ubicacion;
import com.seb.msinventario.infrastructure.adapter.in.web.mapper.InventarioWebMapper;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.UUID;

@Service
@Slf4j
@AllArgsConstructor
public class InventarioService implements InventarioInputPort {
    private InventarioOutputPort inventarioOutputPort;
    private InventarioWebMapper  inventarioWebMapper;
    @Override
    public Inventario guardarInventario(InventarioInputCommand inventarioInputCommand) {
        Inventario inventario = inventarioWebMapper.toDomain(inventarioInputCommand);
        return inventarioOutputPort.guardarInventario(inventario);
    }

    @Override
    public Inventario guardarInventario(Inventario inventario) {
        return inventarioOutputPort.guardarInventario(inventario);
    }
    @Transactional
    @Override
    public Inventario actualizarInventario(UUID id, InventarioInputCommand inventarioInputCommand) {
        Inventario inventarioOptional = inventarioOutputPort.obtenerInventario(id).orElseThrow(
                () -> new InventoryNotFoundException(id)
        );
        Ubicacion ubicacion = inventarioWebMapper.toDomain(inventarioInputCommand.ubicacion());
        inventarioOptional.setNombre(inventarioInputCommand.nombre());
        inventarioOptional.setUbicacion(ubicacion);
        return inventarioOutputPort.guardarInventario(inventarioOptional);
    }

    @Override
    public Inventario obtenerInventario(UUID inventarioId) {
        return inventarioOutputPort.obtenerInventario(inventarioId).orElseThrow(
                () -> new InventoryNotFoundException(inventarioId)
        );
    }

    @Override
    public List<Inventario> obtenerInventarios() {
        return inventarioOutputPort.obtenerInventarios();
    }

    @Override
    public void eliminarInventario(UUID inventarioId) {
        Inventario inventario = inventarioOutputPort.obtenerInventario(inventarioId).orElseThrow(
                () -> new InventoryNotFoundException(inventarioId)
        );
        inventarioOutputPort.eliminarInventario(inventarioId);
    }

}
