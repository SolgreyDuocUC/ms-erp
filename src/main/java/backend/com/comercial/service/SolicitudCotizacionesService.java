package backend.com.comercial.service;

import backend.com.comercial.dto.SolicitudCotizacionesCreateDTO;
import backend.com.comercial.dto.SolicitudCotizacionesDTO;

import java.util.List;
import java.util.Optional;

public interface SolicitudCotizacionesService {
    SolicitudCotizacionesDTO create(SolicitudCotizacionesCreateDTO dto);

    SolicitudCotizacionesDTO update(Long id, SolicitudCotizacionesCreateDTO dto);

    Optional<SolicitudCotizacionesDTO> findById(Long id);

    List<SolicitudCotizacionesDTO> findAll();

    void delete(Long id);
}
