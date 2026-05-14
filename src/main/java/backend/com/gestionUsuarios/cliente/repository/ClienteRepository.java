package backend.com.gestionUsuarios.cliente.repository;

import backend.com.gestionUsuarios.cliente.repository.jpa.entity.ClienteJpaEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ClienteRepository extends JpaRepository<ClienteJpaEntity, Long> {

    Optional<ClienteJpaEntity> findByRunCliente(String runCliente);

}