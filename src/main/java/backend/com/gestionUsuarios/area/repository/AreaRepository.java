package backend.com.gestionUsuarios.area.repository;

import backend.com.gestionUsuarios.area.model.Area;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface AreaRepository extends JpaRepository<Area, Long> {

    Optional<Area> findByNombre(String nombre);

    boolean existsByNombre(String nombre);
}
