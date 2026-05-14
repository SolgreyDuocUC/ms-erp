package backend.com.adquisiciones.infrastructure.persistence.jpa.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Entity
@Table(name = "item_orden_compra")
@Getter
@Setter
public class ItemOCJpaEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idOCItem;

    @Column(unique = true, precision = 20, nullable = false)
    private Long nroItem;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "orden_compra_id", nullable = false)
    private OrdenCompraJpaEntity ordenCompra;

    private Long productoId;

    @Column(length = 255)
    private String descripcionProducto;

    @Column(nullable = false)
    private Integer cantidad;

    @Column(precision = 15, scale = 2)
    private BigDecimal precioUnitarioMonto;

    @Column(length = 3)
    private String precioUnitarioMoneda;
}
