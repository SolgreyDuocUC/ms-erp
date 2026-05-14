package backend.com.adquisiciones.domain.model;

import backend.com.shared.valueobjects.Money;
import lombok.Value;

@Value
public class ItemOC {

    Long idOCItem;
    Long nroItem;
    Long productoId;
    String descripcion;
    Integer cantidad;
    Money precioUnitario;
}
