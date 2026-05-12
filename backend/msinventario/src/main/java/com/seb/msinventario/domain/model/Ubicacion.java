package com.seb.msinventario.domain.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Ubicacion {
    private String calle;
    private String numero;
    private String ciudad;
    private BigDecimal latitude;
    private BigDecimal longitude;
}
