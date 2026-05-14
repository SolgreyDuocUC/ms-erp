package backend.com.produccion.infrastructure.persistence.jpa.repository;

import backend.com.produccion.infrastructure.persistence.jpa.entity.CosteoJpaEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CosteoJpaRepository extends JpaRepository<CosteoJpaEntity, Long> {
    Optional<CosteoJpaEntity> findBySolicitudCostosId(Long solicitudCostosId);
    java.util.List<CosteoJpaEntity> findAllBySolicitudCostosId(Long solicitudCostosId);
}
