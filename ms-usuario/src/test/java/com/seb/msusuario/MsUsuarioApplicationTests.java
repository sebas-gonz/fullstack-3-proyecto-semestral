package com.seb.msusuario;

import com.seb.msusuario.domain.model.RolUsuario;
import com.seb.msusuario.domain.model.Usuario;
import com.seb.msusuario.infrastructure.adapter.out.persistence.UsuarioPersistenceAdapter;
import com.seb.msusuario.infrastructure.adapter.out.persistence.entity.DireccionEntity;
import com.seb.msusuario.infrastructure.adapter.out.persistence.entity.UsuarioEntity;
import com.seb.msusuario.infrastructure.adapter.out.persistence.mapper.UsuarioMapper;
import com.seb.msusuario.infrastructure.adapter.out.persistence.repository.DireccionRepository;
import com.seb.msusuario.infrastructure.adapter.out.persistence.repository.UsuarioRepository;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.IntStream;
@Slf4j
@SpringBootTest(properties = {// Reemplaza esto por el nombre exacto de tu property
        "GEOCODING_KEY=key"
        })
class MsUsuarioApplicationTests {
    @Autowired
    private UsuarioRepository usuarioRepository;
    @Autowired
    private DireccionRepository direccionRepository;

    @Autowired
    private UsuarioMapper usuarioMapper;

    @Test
	void contextLoads() {
	}
    @Disabled
    @Test
    void crearUsuarios() {

        RolUsuario[] roles = RolUsuario.values();
        int numeroRoles = roles.length;
        IntStream.range(0, 10).forEach(i ->{
            var usuarioEntity = UsuarioEntity.builder().nombre("usuario" + i)
                    .apellido("apellido" + i)
                    .email("correo" + i + "@email.com")
                    .idAuth0("idAuth0" + i)
                    .rol(roles[i % numeroRoles])
                    .build();
            usuarioRepository.save(usuarioEntity);
        });
        log.info("Usuarios creados");
    }

    @Test
    void crearDirecciones() {
        List<UsuarioEntity> usuarios = usuarioRepository.findAll();
        if (usuarios.isEmpty()) {
            log.warn("No usuarios creados");
            return;
        }
        IntStream.range(0, usuarios.size()).forEach(i ->{
            UsuarioEntity usuarioEntity = usuarios.get(i);
            var direccionEntity = DireccionEntity.builder()
                    .calle("Moneda")
                    .ciudad("Santiago")
                    .numero(String.valueOf(1202))
                    .pais("Chile")
                    .latitude(new BigDecimal("-33.443018"))
                    .longitude(new BigDecimal("-70.65387"))
                    .usuario(usuarioEntity)
                    .build();
            direccionRepository.save(direccionEntity);
        });
    }

}
