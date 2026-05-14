package backend.com.comercial.repository.jpa.spring;

import backend.com.comercial.repository.jpa.entity.SolicitudCotizacionJpaEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SolicitudCotizacionJpaRepository extends JpaRepository<SolicitudCotizacionJpaEntity, Long> {
    List<SolicitudCotizacionJpaEntity> findByEstado(String estado);
    long countByTipo(String tipo);
}


