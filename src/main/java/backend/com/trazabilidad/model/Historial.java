package backend.com.trazabilidad.model;

import backend.com.gestionUsuarios.usuario.domain.model.User;
import backend.com.shared.domain.jpa.entity.BaseEntity;
import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "trazabilidad_historial")
@Data
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
public class Historial extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 100)
    private String entidad;

    @Column(name = "entidad_id", nullable = false)
    private Long entidadId;

    @Column(nullable = false, length = 50)
    private String accion;

    @Column(name = "estado_anterior", length = 50)
    private String estadoAnterior;

    @Column(name = "estado_nuevo", length = 50)
    private String estadoNuevo;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "usuario_id")
    private User user;

    @Column(columnDefinition = "TEXT")
    private String observacion;
}
