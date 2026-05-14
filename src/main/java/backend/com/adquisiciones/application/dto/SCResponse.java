package backend.com.adquisiciones.application.dto;

import backend.com.adquisiciones.domain.model.SolicitudCompra;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Data
@Builder
public class SCResponse {
    private Long idSC;
    private String numeroSC;
    private Long tenantId;
    private Long notaVentaId;
    private String estado;
    private LocalDate fechaEmision;
    private List<SCItemDTO> items;

    public static SCResponse fromDomain(SolicitudCompra sc) {
        List<SCItemDTO> itemDTOs = sc.getItems().stream().map(item -> {
            SCItemDTO dto = new SCItemDTO();
            dto.setProductoId(item.getProductoId());
            dto.setDescripcionProducto(item.getDescripcion());
            dto.setCantidad(item.getCantidad());
            dto.setPrecioEstimadoUnitario(item.getPrecioEstimadoUnitario().getAmount());
            dto.setMoneda(item.getPrecioEstimadoUnitario().getCurrency());
            return dto;
        }).collect(Collectors.toList());

        return SCResponse.builder()
                .idSC(sc.getIdSC())
                .numeroSC(sc.getNumeroSC() != null ? sc.getNumeroSC().getValue() : null)
                .tenantId(sc.getTenantId() != null ? sc.getTenantId().getValue() : null)
                .notaVentaId(sc.getNotaVentaId())
                .estado(sc.getEstado().name())
                .fechaEmision(sc.getFechaEmision())
                .items(itemDTOs)
                .build();
    }
}
