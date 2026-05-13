package seb.com.msenvio.domain.model;

import lombok.*;

import java.time.Instant;
import java.util.UUID;

@Setter @Getter
@Builder
@AllArgsConstructor
public class Envio {
    @Setter(AccessLevel.NONE) @Builder.Default
    private UUID envioId = UUID.randomUUID();
    private UUID pedidoId;
    private UUID repartidorId;
    private EstadoEnvio estado;
    private Ubicacion destino;
    private Ubicacion origen;
    private Instant fechaRegistro;
    private Instant fechaModificacion;
    public void asignarRepartidor(UUID repartidorId){
        this.repartidorId = repartidorId;
    }
    public void cambiarEstado(EstadoEnvio estado){
        this.estado = estado;
    }

}
