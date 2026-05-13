package com.seb.mscatalogo;

import com.seb.mscatalogo.domain.model.Categoria;
import com.seb.mscatalogo.infrastructure.adapter.out.persistence.entity.CategoriaEntity;
import com.seb.mscatalogo.infrastructure.adapter.out.persistence.entity.ProductoEntity;
import com.seb.mscatalogo.infrastructure.adapter.out.persistence.repository.CategoriaRepository;
import com.seb.mscatalogo.infrastructure.adapter.out.persistence.repository.ProductoRepository;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.IntStream;
@Slf4j
@SpringBootTest
class MsTemplateApplicationTests {
    @Autowired
    private CategoriaRepository categoriaRepository;
    @Autowired
    private ProductoRepository productoRepository;
	@Test
	void contextLoads() {
	}
    @Test
    @Disabled
    void crearCategorias(){
        IntStream.range(0,5).forEach(i ->{
            CategoriaEntity categoriaEntity = CategoriaEntity.builder()
                    .nombre("nombre" + i)
                    .descripcion("descripcion" +1)
                    .build();
            categoriaRepository.save(categoriaEntity);

        });
        log.info("categorias creadas");
    }
    @Test
    @Disabled
    void crearProducto(){
        List<CategoriaEntity> categorias = categoriaRepository.findAll();
        if (categorias.isEmpty()) {
            log.warn("No categorias creadas");
        }
        IntStream.range(0,categorias.size()).forEach(i ->{
            ProductoEntity productoEntity = ProductoEntity.builder()
                    .nombre("producto " + i)
                    .descripcion("descripcion" + i)
                    .sku("sku" + i)
                    .precioBase(BigDecimal.valueOf(i))
                    .categoria(categorias.get(i))
                    .build();
            productoRepository.save(productoEntity);
        });
        log.info("productos creados");
    }

}
