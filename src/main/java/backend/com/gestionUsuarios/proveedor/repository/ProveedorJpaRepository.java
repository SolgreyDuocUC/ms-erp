package backend.com.gestionUsuarios.proveedor.repository;

import backend.com.gestionUsuarios.proveedor.domain.JPA.ProveedorJpaEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ProveedorJpaRepository extends JpaRepository<ProveedorJpaEntity, Long> {
    Optional<ProveedorJpaEntity> findByRutProveedor(String rutProveedor);
}
