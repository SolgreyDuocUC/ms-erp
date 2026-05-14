package backend.com.adquisiciones.infrastructure.persistence.jpa.entity;

import backend.com.adquisiciones.domain.model.TipoItemSC;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Entity
@Table(name = "item_solicitud_compra")
@Getter
@Setter
public class ItemSCJpaEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idSCItem;

    @Column(name = "nro_item", nullable = false)
    private Long nroItem;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "solicitud_compra_id", nullable = false)
    private SolicitudCompraJpaEntity solicitudCompra;

    private Long productoId;

    @Column(length = 255)
    private String descripcionProducto;

    @Column(nullable = false)
    private Integer cantidad;

    @Column(precision = 15, scale = 2)
    private BigDecimal precioEstimadoMonto;

    @Column(length = 3)
    private String precioEstimadoMoneda;

    @Enumerated(EnumType.STRING)
    @Column(length = 20)
    private TipoItemSC tipo;
}
