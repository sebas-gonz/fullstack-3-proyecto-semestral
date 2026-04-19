package com.seb.msusuario.infrastructure.adapter.out.persistence.mapper;

import com.seb.msusuario.domain.model.Usuario;
import com.seb.msusuario.infrastructure.adapter.out.persistence.entity.UsuarioEntity;
import org.mapstruct.AfterMapping;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;
import org.mapstruct.MappingTarget;

import java.util.ArrayList;
import java.util.List;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING, uses = {DireccionMapper.class})
public interface UsuarioMapper {
    UsuarioEntity toEntity(Usuario usuario);
    Usuario toDomain(UsuarioEntity usuarioEntity);
    List<Usuario> toDomain(List<UsuarioEntity> usuarios);
    List<UsuarioEntity> toEntity(List<Usuario> usuarios);
    @AfterMapping
    default void direcctionMapper(@MappingTarget UsuarioEntity usuarioEntity){
        if (usuarioEntity.getDirecciones() == null){
            usuarioEntity.setDirecciones(new ArrayList<>());
        } else {
            usuarioEntity.getDirecciones().forEach(direccionEntity -> {
                direccionEntity.setUsuario(usuarioEntity);
            });
        }
    }
}
