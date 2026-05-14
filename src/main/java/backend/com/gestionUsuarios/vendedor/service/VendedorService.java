package backend.com.gestionUsuarios.vendedor.service;

import backend.com.gestionUsuarios.vendedor.dto.VendedorCreateDTO;
import backend.com.gestionUsuarios.vendedor.dto.VendedorDTO;

import java.util.List;

public interface VendedorService {
    VendedorDTO create(VendedorCreateDTO dto);
    VendedorDTO update(Long id, VendedorCreateDTO dto);
    VendedorDTO findById(Long id);
    List<VendedorDTO> findAll();
    void delete(Long id);
    VendedorDTO findByUsuarioId(Long usuarioId);
}
