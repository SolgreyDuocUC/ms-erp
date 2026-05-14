package backend.com.adquisiciones.domain.ports;

import backend.com.adquisiciones.domain.model.EstadoSC;
import backend.com.adquisiciones.domain.model.SolicitudCompra;

import java.util.List;
import java.util.Optional;

public interface SolicitudCompraRepository {
    SolicitudCompra save(SolicitudCompra sc);

    Optional<SolicitudCompra> findById(Long id);

    List<SolicitudCompra> findByEstado(EstadoSC estado);

    List<SolicitudCompra> findByNotaVentaId(Long notaVentaId);

    List<SolicitudCompra> findAll();

    java.util.Optional<Long> findMaxNumero();
}
