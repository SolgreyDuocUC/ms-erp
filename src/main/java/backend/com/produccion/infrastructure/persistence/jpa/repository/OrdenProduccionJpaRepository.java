package backend.com.produccion.infrastructure.persistence.jpa.repository;

import backend.com.produccion.infrastructure.persistence.jpa.entity.OrdenProduccionJpaEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OrdenProduccionJpaRepository extends JpaRepository<OrdenProduccionJpaEntity, Long> {
    List<OrdenProduccionJpaEntity> findByNotaVentaId(Long notaVentaId);
}
