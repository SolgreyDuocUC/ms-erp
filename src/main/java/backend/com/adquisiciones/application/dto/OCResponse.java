package backend.com.adquisiciones.application.dto;

import backend.com.adquisiciones.domain.model.OrdenCompra;
import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Data
@Builder
public class OCResponse {
    private Long idOC;
    private String numeroOC;
    private Long tenantId;
    private Long solicitudCompraId;
    private Long ordenProduccionId;
    private Long proveedorId;
    private String tipo;
    private String estado;
    private BigDecimal montoTotal;
    private String monedaMontoTotal;
    private LocalDate fechaEmision;
    private LocalDate fechaRecepcion;
    private List<OCItemDTO> items;

    public static OCResponse fromDomain(OrdenCompra oc) {
        List<OCItemDTO> itemDTOs = oc.getItems().stream().map(item -> {
            OCItemDTO dto = new OCItemDTO();
            dto.setIdOCItem(item.getIdOCItem());
            dto.setNroItem(item.getNroItem());
            dto.setProductoId(item.getProductoId());
            dto.setDescripcionProducto(item.getDescripcion());
            dto.setCantidad(item.getCantidad());
            dto.setPrecioUnitario(item.getPrecioUnitario().getAmount());
            dto.setMoneda(item.getPrecioUnitario().getCurrency());
            return dto;
        }).collect(Collectors.toList());

        return OCResponse.builder()
                .idOC(oc.getIdOC())
                .numeroOC(oc.getNumeroOC() != null ? oc.getNumeroOC().getValue() : null)
                .tenantId(oc.getTenantId() != null ? oc.getTenantId().getValue() : null)
                .solicitudCompraId(oc.getSolicitudCompraId())
                .ordenProduccionId(oc.getOrdenProduccionId())
                .proveedorId(oc.getProveedorId())
                .tipo(oc.getTipo().name())
                .estado(oc.getEstado().name())
                .montoTotal(oc.getMontoTotal().getAmount())
                .monedaMontoTotal(oc.getMontoTotal().getCurrency())
                .fechaEmision(oc.getFechaEmision())
                .fechaRecepcion(oc.getFechaRecepcion())
                .items(itemDTOs)
                .build();
    }
}
