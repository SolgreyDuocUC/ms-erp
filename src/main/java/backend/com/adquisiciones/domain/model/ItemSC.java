package backend.com.adquisiciones.domain.model;

import backend.com.shared.valueobjects.Money;
import lombok.Value;

@Value
public class ItemSC {

    Long idSCItem;
    Long nroItem;
    Long productoId;
    String descripcion;
    Integer cantidad;
    Money precioEstimadoUnitario;
    TipoItemSC tipo;
}
