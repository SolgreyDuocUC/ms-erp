package backend.com.comercial.repository.jpa.spring;

import backend.com.comercial.repository.jpa.entity.NotaVentaJpaEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface NotaVentaJpaRepository extends JpaRepository<NotaVentaJpaEntity, Long> {
    List<NotaVentaJpaEntity> findByEstado(String estado);

    @org.springframework.data.jpa.repository.Query("SELECT MAX(CAST(n.numeroNV AS long)) FROM NotaVentaJpaEntity n")
    java.util.Optional<Long> findMaxNumero();
}
