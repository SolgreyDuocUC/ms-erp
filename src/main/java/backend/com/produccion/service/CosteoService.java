package backend.com.produccion.service;

import backend.com.produccion.dto.CosteoDTO;
import java.util.Optional;

public interface CosteoService {
    CosteoDTO save(CosteoDTO costeoDTO);
    Optional<CosteoDTO> findBySolicitudCostosId(Long scosId);
    java.util.List<CosteoDTO> findAllBySolicitudCostosId(Long scosId);
}
