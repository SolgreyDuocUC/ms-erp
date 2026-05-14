package backend.com.adquisiciones.domain.ports;

import backend.com.adquisiciones.domain.model.OrdenCompra;
import backend.com.adquisiciones.domain.model.EstadoOC;

import java.util.List;
import java.util.Optional;

public interface OrdenCompraRepository {
    OrdenCompra save(OrdenCompra oc);
    Optional<OrdenCompra> findById(Long id);
    List<OrdenCompra> findByEstado(EstadoOC estado);
    List<OrdenCompra> findByOrdenProduccionId(Long opId);
}


