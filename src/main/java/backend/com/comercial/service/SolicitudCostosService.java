package backend.com.comercial.service;

import backend.com.comercial.dto.SolicitudCostosCreateDTO;
import backend.com.comercial.dto.SolicitudCostosDTO;

import java.util.List;
import java.util.Optional;

public interface SolicitudCostosService {


    SolicitudCostosDTO create(SolicitudCostosCreateDTO dto);

    SolicitudCostosDTO update(Long id, SolicitudCostosCreateDTO dto);

    Optional<SolicitudCostosDTO> findById(Long id);

    List<SolicitudCostosDTO> findAll();

    void delete(Long id);
}
