package backend.com.gestionUsuarios.proveedor.service;

import backend.com.gestionUsuarios.proveedor.repository.ProveedorRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class ProveedorValidator {

    private final ProveedorRepository proveedorRepository;

    public void validateUniqueness(String rutProveedor) {
        if (proveedorRepository.findByRutProveedor(rutProveedor).isPresent()) {
            throw new RuntimeException("Ya existe un proveedor con RUT: " + rutProveedor);
        }
    }
}
