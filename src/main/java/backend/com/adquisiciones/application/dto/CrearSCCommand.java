package backend.com.adquisiciones.application.dto;

import lombok.Data;
import java.util.List;

@Data
public class CrearSCCommand {
    private Long tenantId;
    private Long notaVentaId;
    private List<SCItemDTO> items;
}


