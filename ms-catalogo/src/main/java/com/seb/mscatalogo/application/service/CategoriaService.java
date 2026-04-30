package com.seb.mscatalogo.application.service;

import com.seb.mscatalogo.application.exception.CategoryNotFoundException;
import com.seb.mscatalogo.application.port.in.CategoriaInputPort;
import com.seb.mscatalogo.application.port.in.command.CategoriaWebRequestCommand;
import com.seb.mscatalogo.application.port.out.CategoriaOutputPort;
import com.seb.mscatalogo.domain.model.Categoria;
import com.seb.mscatalogo.infrastructure.adapter.in.web.mapper.CategoriaWebMapper;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Slf4j
@Service
@AllArgsConstructor
public class CategoriaService implements CategoriaInputPort {
    private final CategoriaOutputPort categoriaOutputPort;
    private final CategoriaWebMapper  categoriaWebMapper;
    @Override
    public Categoria crearCategoria(CategoriaWebRequestCommand  categoriaWebRequestCommand) {
        Categoria categoria = Categoria.builder().nombre(categoriaWebRequestCommand.nombre())
                .descripcion(categoriaWebRequestCommand.descripcion()).build();
        return categoriaOutputPort.guardarCategoria(categoria);
    }

    @Override
    public Categoria actualizarCategoria(UUID categoriaId, CategoriaWebRequestCommand categoriaWebRequestCommand) {
        Categoria categoriaOptional = categoriaOutputPort.obtenerCategoriaPorId(categoriaId)
                .orElseThrow(() -> new CategoryNotFoundException(categoriaId));
        categoriaOptional.setNombre(categoriaWebRequestCommand.nombre());
        categoriaOptional.setDescripcion(categoriaWebRequestCommand.descripcion());
        return categoriaOutputPort.guardarCategoria(categoriaOptional);
    }

    @Override
    public Categoria obtenerCategoria(UUID categoriaId) {
        return categoriaOutputPort.obtenerCategoriaPorId(categoriaId)
                .orElseThrow(() -> new CategoryNotFoundException(categoriaId));
    }

    @Override
    public List<Categoria> obtenerTodosCategorias() {
        return categoriaOutputPort.obtenerTodosCategorias();
    }

    @Override
    public void eliminarCategoria(UUID categoriaId) {
        Categoria categoria  = categoriaOutputPort.obtenerCategoriaPorId(categoriaId).orElseThrow(
                () -> new CategoryNotFoundException(categoriaId)
        );
        categoriaOutputPort.eliminarCategoria(categoriaId);
    }
}
