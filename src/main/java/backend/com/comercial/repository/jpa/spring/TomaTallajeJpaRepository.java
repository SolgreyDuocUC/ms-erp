package backend.com.comercial.repository.jpa.spring;

import backend.com.comercial.repository.jpa.entity.TomaTallajeJpaEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TomaTallajeJpaRepository extends JpaRepository<TomaTallajeJpaEntity, Long> {
}
