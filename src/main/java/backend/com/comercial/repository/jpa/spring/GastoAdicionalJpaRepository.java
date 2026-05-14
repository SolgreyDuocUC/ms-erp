package backend.com.comercial.repository.jpa.spring;

import backend.com.comercial.repository.jpa.entity.GastoAdicionalJpaEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface GastoAdicionalJpaRepository extends JpaRepository<GastoAdicionalJpaEntity, Long> {
}
