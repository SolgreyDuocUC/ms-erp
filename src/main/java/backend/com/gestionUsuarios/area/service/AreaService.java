package backend.com.gestionUsuarios.area.service;

import backend.com.gestionUsuarios.area.model.Area;
import java.util.List;

public interface AreaService {
    Area obtenerArea(Long id);
    List<Area> listarAreas();
    Area crearArea(Area area);
    Area actualizarArea(Long id, Area area);
    void eliminarArea(Long id);
}