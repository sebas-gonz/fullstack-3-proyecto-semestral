package com.seb.msinventario;

import com.seb.msinventario.domain.model.Ubicacion;
import com.seb.msinventario.infrastructure.adapter.out.persistence.entity.InventarioEntity;
import com.seb.msinventario.infrastructure.adapter.out.persistence.entity.StockEntity;
import com.seb.msinventario.infrastructure.adapter.out.persistence.entity.UbicacionEntity;
import com.seb.msinventario.infrastructure.adapter.out.persistence.repository.InventarioEntityRepository;
import com.seb.msinventario.infrastructure.adapter.out.persistence.repository.StockEntityRepository;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.List;
import java.util.UUID;
import java.util.stream.IntStream;

@Slf4j
@SpringBootTest
class MsTemplateApplicationTests {
    @Autowired
    private InventarioEntityRepository inventarioEntityRepository;
    @Autowired
    private StockEntityRepository stockEntityRepository;
	@Test
	void contextLoads() {
	}
    @Test
    @Disabled
    void crearInventarios(){
        IntStream.range(0,5).forEach(i -> {
        UbicacionEntity ubicacionEntity = UbicacionEntity.builder()
                .calle("Moneda")
                .ciudad("Santiago")
                .numero(String.valueOf(1202))
                .latitude(new BigDecimal("-33.443018"))
                .longitude(new BigDecimal("-70.65387"))
                .build();
         InventarioEntity inventario = InventarioEntity.builder()
                 .inventarioId(UUID.randomUUID())
                 .nombre("nombre" + i)
                 .ubicacion(ubicacionEntity)
                 .build();
         inventarioEntityRepository.save(inventario);
        });
    }
    @Test
    @Transactional
    @Rollback(false)
    void crearStocks(){
        List<InventarioEntity> inventarios = inventarioEntityRepository.findAll();
        IntStream.range(0, inventarios.size()).forEach(i -> {
            StockEntity stockEntity = StockEntity.builder()
                    .stockId(UUID.randomUUID())
                    .lote("lote" +i)
                    .productoId(UUID.randomUUID())
                    .cantidad(i)
                    .fechaRegistro(Instant.now())
                    .build();
            inventarios.get(i).getStocks().add(stockEntity);
        });
        inventarioEntityRepository.saveAll(inventarios);
    }
}
