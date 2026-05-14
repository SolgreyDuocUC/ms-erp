package backend.com.shared.domain.jpa.repository;

import backend.com.shared.domain.jpa.entity.ProductoJpaEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.Optional;

@Repository
public interface ProductoJpaRepository extends JpaRepository<ProductoJpaEntity, Long> {
    Optional<ProductoJpaEntity> findByCodigoProducto(String codigoProducto);
}
