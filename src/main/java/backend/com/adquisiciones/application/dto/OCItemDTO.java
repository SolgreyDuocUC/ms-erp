package backend.com.adquisiciones.application.dto;

import lombok.Data;
import java.math.BigDecimal;

@Data
public class OCItemDTO {

    private Long idOCItem;
    private Long nroItem;
    private Long productoId;
    private String descripcionProducto;
    private Integer cantidad;
    private BigDecimal precioUnitario;
    private String moneda;
}
