package backend.com.adquisiciones.application.dto;

import backend.com.adquisiciones.domain.model.TipoOC;
import lombok.Data;
import java.util.List;

@Data
public class EmitirOCCommand {
    private Long tenantId;
    private Long solicitudCompraId;
    private Long ordenProduccionId;
    private Long proveedorId;
    private TipoOC tipo;
    private List<OCItemDTO> items;
}


