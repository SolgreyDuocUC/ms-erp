package backend.com.adquisiciones.infrastructure.persistence.jpa.repository;

import backend.com.adquisiciones.infrastructure.persistence.jpa.entity.OrdenCompraJpaEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OrdenCompraJpaRepository extends JpaRepository<OrdenCompraJpaEntity, Long> {
    List<OrdenCompraJpaEntity> findByEstado(String estado);
    List<OrdenCompraJpaEntity> findByOrdenProduccionId(Long ordenProduccionId);
}


