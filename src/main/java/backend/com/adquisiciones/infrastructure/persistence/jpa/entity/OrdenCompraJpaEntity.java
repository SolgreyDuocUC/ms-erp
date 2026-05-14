package backend.com.adquisiciones.infrastructure.persistence.jpa.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "orden_compra")
@Getter
@Setter
public class OrdenCompraJpaEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idOC;

    @Column(unique = true, length = 20, nullable = false)
    private String numeroOC;

    private Long tenantId;
    private Long solicitudCompraId;
    private Long ordenProduccionId;
    private Long proveedorId;

    @Column(length = 30)
    private String tipo;

    @Column(length = 30)
    private String estado;

    @Column(precision = 15, scale = 2)
    private BigDecimal montoTotal;

    @Column(length = 3)
    private String monedaMontoTotal;

    private LocalDate fechaEmision;
    private LocalDate fechaRecepcion;

    @OneToMany(mappedBy = "ordenCompra", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ItemOCJpaEntity> items = new ArrayList<>();

    public void addItem(ItemOCJpaEntity item) {
        items.add(item);
        item.setOrdenCompra(this);
    }
}
