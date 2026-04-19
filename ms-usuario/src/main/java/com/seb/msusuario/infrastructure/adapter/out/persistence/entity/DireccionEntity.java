package com.seb.msusuario.infrastructure.adapter.out.persistence.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.envers.Audited;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "direccion")
@Getter @Setter
@AllArgsConstructor
@Builder
@NoArgsConstructor
@Audited
@EntityListeners(AuditingEntityListener.class)
public class DireccionEntity {
    @Id @Setter(AccessLevel.NONE)
    private String id;
    private String calle;
    private String numero;
    private String ciudad;
    private BigDecimal latitud;
    private BigDecimal longitud;
    @Column(updatable = true) @LastModifiedDate
    private LocalDateTime fechaModificacion;
    @Column(updatable = false) @CreatedDate
    private LocalDateTime fechaRegistro;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "usuario_id")
    private UsuarioEntity usuario;
}
