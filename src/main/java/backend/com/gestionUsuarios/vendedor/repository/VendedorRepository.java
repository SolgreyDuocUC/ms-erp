package backend.com.gestionUsuarios.vendedor.repository;

import backend.com.gestionUsuarios.vendedor.repository.jpa.entity.VendedorJpaEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface VendedorRepository extends JpaRepository<VendedorJpaEntity, Long> {
    Optional<VendedorJpaEntity> findByCodigoVendedor(String codigoVendedor);

    Optional<VendedorJpaEntity> findByUsuario_UsuarioId(Long usuarioId);

    boolean existsByCodigoVendedor(String codigoVendedor);
}
