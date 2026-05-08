package seb.com.msenvio.domain.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
@Setter
@Getter
@Builder
@AllArgsConstructor
public class Ubicacion {
    private String calle;
    private String numero;
    private String ciudad;
    private BigDecimal latitude;
    private BigDecimal longitude;
}
