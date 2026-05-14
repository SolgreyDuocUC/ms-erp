package backend.com.adquisiciones.infrastructure.persistence.jpa.repository;

import backend.com.adquisiciones.infrastructure.persistence.jpa.entity.SolicitudCompraJpaEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SolicitudCompraJpaRepository extends JpaRepository<SolicitudCompraJpaEntity, Long> {
    List<SolicitudCompraJpaEntity> findByEstado(String estado);

    List<SolicitudCompraJpaEntity> findByNotaVentaId(Long notaVentaId);

    @org.springframework.data.jpa.repository.Query("SELECT MAX(CAST(s.numeroSC AS long)) FROM SolicitudCompraJpaEntity s")
    java.util.Optional<Long> findMaxNumero();
}
