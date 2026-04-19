package com.seb.msusuario.infrastructure.adapter.out.persistence.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.envers.Audited;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "usuario")
@Getter @Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Audited
@EntityListeners(AuditingEntityListener.class)
public class UsuarioEntity {
    @Id @Setter(AccessLevel.NONE)
    private String id;
    @Column(unique = true)
    private String idAuth0;
    private String nombre;
    private String apellido;
    @Column(nullable = false, unique = true)
    private String email;
    @Column(nullable = false, updatable = false) @CreatedDate
    private LocalDateTime fechaRegistro;
    @LastModifiedDate
    private LocalDateTime fechaModificacion;
    @OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL,mappedBy = "usuario", orphanRemoval = true)
    private List<DireccionEntity> direcciones = new ArrayList<DireccionEntity>();
}
