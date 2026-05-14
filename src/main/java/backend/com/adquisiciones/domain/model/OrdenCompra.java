package backend.com.adquisiciones.domain.model;

import backend.com.shared.events.DomainEvent;
import backend.com.shared.valueobjects.DocumentNumber;
import backend.com.shared.valueobjects.Money;
import backend.com.shared.valueobjects.TenantId;
import backend.com.adquisiciones.domain.events.OCEmitidaEvent;
import backend.com.adquisiciones.domain.events.OCRecepcionadaEvent;
import lombok.Getter;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Getter
public class OrdenCompra {
    private Long idOC;
    private DocumentNumber numeroOC;
    private TenantId tenantId;
    private Long solicitudCompraId;
    private Long ordenProduccionId;
    private Long proveedorId;
    private TipoOC tipo;
    private EstadoOC estado;
    private Money montoTotal;
    private LocalDate fechaEmision;
    private LocalDate fechaRecepcion;
    private List<ItemOC> items = new ArrayList<>();

    private transient List<DomainEvent> domainEvents = new ArrayList<>();

    private OrdenCompra(DocumentNumber numeroOC, TenantId tenantId, Long solicitudCompraId, Long ordenProduccionId,
            Long proveedorId, TipoOC tipo, List<ItemOC> items) {
        this.numeroOC = numeroOC;
        this.tenantId = tenantId;
        this.solicitudCompraId = solicitudCompraId;
        this.ordenProduccionId = ordenProduccionId;
        this.proveedorId = proveedorId;
        this.tipo = tipo;
        this.estado = EstadoOC.EMITIDA;
        this.fechaEmision = LocalDate.now();
        if (items != null) {
            this.items.addAll(items);
        }
        calcularTotales();
    }

    public OrdenCompra(Long idOC, DocumentNumber numeroOC, TenantId tenantId, Long solicitudCompraId,
            Long ordenProduccionId, Long proveedorId, TipoOC tipo, EstadoOC estado, Money montoTotal,
            LocalDate fechaEmision, LocalDate fechaRecepcion, List<ItemOC> items) {
        this.idOC = idOC;
        this.numeroOC = numeroOC;
        this.tenantId = tenantId;
        this.solicitudCompraId = solicitudCompraId;
        this.ordenProduccionId = ordenProduccionId;
        this.proveedorId = proveedorId;
        this.tipo = tipo;
        this.estado = estado;
        this.montoTotal = montoTotal;
        this.fechaEmision = fechaEmision;
        this.fechaRecepcion = fechaRecepcion;
        if (items != null) {
            this.items.addAll(items);
        }
    }

    public static OrdenCompra crear(DocumentNumber numero, TenantId tenantId, Long solicitudCompraId,
            Long ordenProduccionId, Long proveedorId, TipoOC tipo, List<ItemOC> items) {
        if (items == null || items.isEmpty()) {
            throw new IllegalArgumentException("La Orden de Compra debe tener al menos un item");
        }
        OrdenCompra oc = new OrdenCompra(numero, tenantId, solicitudCompraId, ordenProduccionId, proveedorId, tipo,
                items);
        oc.addDomainEvent(new OCEmitidaEvent(oc.getIdOC()));
        return oc;
    }

    public void calcularTotales() {
        if (this.items.isEmpty()) {
            this.montoTotal = new Money(BigDecimal.ZERO, "CLP");
            return;
        }

        String currentCurrency = this.items.get(0).getPrecioUnitario().getCurrency();
        Money total = new Money(BigDecimal.ZERO, currentCurrency);

        for (ItemOC item : items) {
            Money itemTotal = item.getPrecioUnitario().multiply(new BigDecimal(item.getCantidad()));
            total = total.add(itemTotal);
        }

        this.montoTotal = total;
    }

    public void recepcionar() {
        if (this.estado != EstadoOC.EMITIDA && this.estado != EstadoOC.EN_TRANSITO) {
            throw new IllegalStateException("La OC no está en estado válido para ser recepcionada");
        }
        this.estado = EstadoOC.RECIBIDA;
        this.fechaRecepcion = LocalDate.now();
        this.addDomainEvent(new OCRecepcionadaEvent(this.idOC, this.ordenProduccionId));
    }

    private void addDomainEvent(DomainEvent event) {
        this.domainEvents.add(event);
    }

    public void clearDomainEvents() {
        this.domainEvents.clear();
    }

    public List<DomainEvent> getDomainEvents() {
        return Collections.unmodifiableList(domainEvents);
    }
}
