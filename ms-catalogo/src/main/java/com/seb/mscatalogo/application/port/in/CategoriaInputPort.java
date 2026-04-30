package com.seb.mscatalogo.application.port.in;

import com.seb.mscatalogo.application.port.in.command.CategoriaWebRequestCommand;
import com.seb.mscatalogo.domain.model.Categoria;

import java.util.List;
import java.util.UUID;

public interface CategoriaInputPort {
    Categoria crearCategoria(CategoriaWebRequestCommand categoriaWebRequestCommand);
    Categoria actualizarCategoria(UUID categoriaId, CategoriaWebRequestCommand categoriaWebRequestCommand);
    Categoria obtenerCategoria(UUID categoriaId);
    List<Categoria> obtenerTodosCategorias();
    void eliminarCategoria(UUID categoriaId);
}
