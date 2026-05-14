package backend.com.adquisiciones.infrastructure.persistence.jpa.mapper;

import backend.com.adquisiciones.domain.model.EstadoOC;
import backend.com.adquisiciones.domain.model.TipoOC;
import backend.com.adquisiciones.domain.model.ItemOC;
import backend.com.adquisiciones.domain.model.OrdenCompra;
import backend.com.adquisiciones.infrastructure.persistence.jpa.entity.ItemOCJpaEntity;
import backend.com.adquisiciones.infrastructure.persistence.jpa.entity.OrdenCompraJpaEntity;
import backend.com.shared.valueobjects.DocumentNumber;
import backend.com.shared.valueobjects.Money;
import backend.com.shared.valueobjects.TenantId;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class OrdenCompraMapper {

    public OrdenCompraJpaEntity toJpaEntity(OrdenCompra domain) {
        if (domain == null)
            return null;

        OrdenCompraJpaEntity entity = new OrdenCompraJpaEntity();
        entity.setIdOC(domain.getIdOC());
        entity.setNumeroOC(domain.getNumeroOC() != null ? domain.getNumeroOC().getValue() : null);
        entity.setTenantId(domain.getTenantId() != null ? domain.getTenantId().getValue() : null);
        entity.setSolicitudCompraId(domain.getSolicitudCompraId());
        entity.setOrdenProduccionId(domain.getOrdenProduccionId());
        entity.setProveedorId(domain.getProveedorId());
        entity.setTipo(domain.getTipo() != null ? domain.getTipo().name() : null);
        entity.setEstado(domain.getEstado() != null ? domain.getEstado().name() : null);

        if (domain.getMontoTotal() != null) {
            entity.setMontoTotal(domain.getMontoTotal().getAmount());
            entity.setMonedaMontoTotal(domain.getMontoTotal().getCurrency());
        }

        entity.setFechaEmision(domain.getFechaEmision());
        entity.setFechaRecepcion(domain.getFechaRecepcion());

        if (domain.getItems() != null) {
            domain.getItems().forEach(item -> entity.addItem(toItemJpaEntity(item)));
        }

        return entity;
    }

    private ItemOCJpaEntity toItemJpaEntity(ItemOC item) {
        if (item == null)
            return null;

        ItemOCJpaEntity entity = new ItemOCJpaEntity();
        entity.setProductoId(item.getProductoId());
        entity.setDescripcionProducto(item.getDescripcion());
        entity.setCantidad(item.getCantidad());
        if (item.getPrecioUnitario() != null) {
            entity.setPrecioUnitarioMonto(item.getPrecioUnitario().getAmount());
            entity.setPrecioUnitarioMoneda(item.getPrecioUnitario().getCurrency());
        }
        return entity;
    }

    public OrdenCompra toDomain(OrdenCompraJpaEntity entity) {
        if (entity == null)
            return null;

        DocumentNumber numero = entity.getNumeroOC() != null ? new DocumentNumber(entity.getNumeroOC()) : null;
        TenantId tenantId = entity.getTenantId() != null ? new TenantId(entity.getTenantId()) : null;
        TipoOC tipo = entity.getTipo() != null ? TipoOC.valueOf(entity.getTipo()) : null;
        EstadoOC estado = entity.getEstado() != null ? EstadoOC.valueOf(entity.getEstado()) : null;

        Money montoTotal = null;
        if (entity.getMontoTotal() != null && entity.getMonedaMontoTotal() != null) {
            montoTotal = new Money(entity.getMontoTotal(), entity.getMonedaMontoTotal());
        }

        List<ItemOC> items = null;
        if (entity.getItems() != null) {
            items = entity.getItems().stream().map(this::toItemDomain).collect(Collectors.toList());
        }

        return new OrdenCompra(
                entity.getIdOC(),
                numero,
                tenantId,
                entity.getSolicitudCompraId(),
                entity.getOrdenProduccionId(),
                entity.getProveedorId(),
                tipo,
                estado,
                montoTotal,
                entity.getFechaEmision(),
                entity.getFechaRecepcion(),
                items);
    }

    private ItemOC toItemDomain(ItemOCJpaEntity entity) {
        if (entity == null)
            return null;

        Money precio = null;
        if (entity.getPrecioUnitarioMonto() != null && entity.getPrecioUnitarioMoneda() != null) {
            precio = new Money(entity.getPrecioUnitarioMonto(), entity.getPrecioUnitarioMoneda());
        }

        return new ItemOC(
                entity.getIdOCItem(),
                entity.getNroItem(),
                entity.getProductoId(),
                entity.getDescripcionProducto(),
                entity.getCantidad(),
                precio);
    }
}
