package backend.com.adquisiciones.infrastructure.persistence.jpa.mapper;

import backend.com.adquisiciones.domain.model.EstadoSC;
import backend.com.adquisiciones.domain.model.ItemSC;
import backend.com.adquisiciones.domain.model.SolicitudCompra;
import backend.com.adquisiciones.infrastructure.persistence.jpa.entity.ItemSCJpaEntity;
import backend.com.adquisiciones.infrastructure.persistence.jpa.entity.SolicitudCompraJpaEntity;
import backend.com.shared.valueobjects.DocumentNumber;
import backend.com.shared.valueobjects.Money;
import backend.com.shared.valueobjects.TenantId;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class SolicitudCompraMapper {

    public SolicitudCompraJpaEntity toJpaEntity(SolicitudCompra domain) {
        if (domain == null)
            return null;

        SolicitudCompraJpaEntity entity = new SolicitudCompraJpaEntity();
        entity.setIdSC(domain.getIdSC());
        entity.setNumeroSC(domain.getNumeroSC() != null ? domain.getNumeroSC().getValue() : null);
        entity.setTenantId(domain.getTenantId() != null ? domain.getTenantId().getValue() : null);
        entity.setNotaVentaId(domain.getNotaVentaId());
        entity.setEstado(domain.getEstado() != null ? domain.getEstado().name() : null);
        entity.setFechaEmision(domain.getFechaEmision());

        if (domain.getItems() != null) {
            domain.getItems().forEach(item -> entity.addItem(toItemJpaEntity(item)));
        }

        return entity;
    }

    private ItemSCJpaEntity toItemJpaEntity(ItemSC item) {
        if (item == null)
            return null;

        ItemSCJpaEntity entity = new ItemSCJpaEntity();
        entity.setNroItem(item.getNroItem());
        entity.setProductoId(item.getProductoId());
        entity.setDescripcionProducto(item.getDescripcion());
        entity.setCantidad(item.getCantidad());
        if (item.getPrecioEstimadoUnitario() != null) {
            entity.setPrecioEstimadoMonto(item.getPrecioEstimadoUnitario().getAmount());
            entity.setPrecioEstimadoMoneda(item.getPrecioEstimadoUnitario().getCurrency());
        }
        entity.setTipo(item.getTipo());
        return entity;
    }

    public SolicitudCompra toDomain(SolicitudCompraJpaEntity entity) {
        if (entity == null)
            return null;

        DocumentNumber numero = entity.getNumeroSC() != null ? new DocumentNumber(entity.getNumeroSC()) : null;
        TenantId tenantId = entity.getTenantId() != null ? new TenantId(entity.getTenantId()) : null;
        EstadoSC estado = entity.getEstado() != null ? EstadoSC.valueOf(entity.getEstado()) : null;

        List<ItemSC> items = null;
        if (entity.getItems() != null) {
            items = entity.getItems().stream().map(this::toItemDomain).collect(Collectors.toList());
        }

        return new SolicitudCompra(
                entity.getIdSC(),
                numero,
                tenantId,
                entity.getNotaVentaId(),
                estado,
                entity.getFechaEmision(),
                items);
    }

    private ItemSC toItemDomain(ItemSCJpaEntity entity) {
        if (entity == null)
            return null;

        Money precio = null;
        if (entity.getPrecioEstimadoMonto() != null && entity.getPrecioEstimadoMoneda() != null) {
            precio = new Money(entity.getPrecioEstimadoMonto(), entity.getPrecioEstimadoMoneda());
        }

        return new ItemSC(
                entity.getIdSCItem(),
                entity.getNroItem(),
                entity.getProductoId(),
                entity.getDescripcionProducto(),
                entity.getCantidad(),
                precio,
                entity.getTipo());
    }
}
