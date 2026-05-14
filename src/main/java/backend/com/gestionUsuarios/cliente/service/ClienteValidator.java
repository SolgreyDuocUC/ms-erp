package backend.com.gestionUsuarios.cliente.service;

import backend.com.gestionUsuarios.cliente.repository.ClienteRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class ClienteValidator {

    private final ClienteRepository clienteRepository;

    public void validateUniqueness(String runCliente) {
        if (clienteRepository.findByRunCliente(runCliente).isPresent()) {
            throw new IllegalArgumentException("Ya existe un cliente con RUN: " + runCliente);
        }
    }
}
