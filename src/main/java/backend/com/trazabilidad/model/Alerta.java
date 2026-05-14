package backend.com.trazabilidad.model;

import backend.com.shared.domain.jpa.entity.BaseEntity;
import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "trazabilidad_alertas")
@Data
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
public class Alerta extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 100)
    private String titulo;

    @Column(nullable = false, columnDefinition = "TEXT")
    private String mensaje;

    @Column(nullable = false, length = 20)
    private String nivel = "INFO"; // INFO, WARNING, CRITICAL

    private boolean leida = false;
}
