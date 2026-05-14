package backend.com.produccion.infrastructure.persistence.jpa.repository;

import backend.com.produccion.infrastructure.persistence.jpa.entity.OrdenTrabajoJpaEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OrdenTrabajoJpaRepository extends JpaRepository<OrdenTrabajoJpaEntity, Long> {
    List<OrdenTrabajoJpaEntity> findByNotaVentaId(Long notaVentaId);
}
