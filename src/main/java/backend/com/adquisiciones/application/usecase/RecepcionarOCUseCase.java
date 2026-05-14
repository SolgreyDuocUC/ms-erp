package backend.com.adquisiciones.application.usecase;

import backend.com.adquisiciones.application.dto.OCResponse;
import backend.com.adquisiciones.application.dto.RecepcionarOCCommand;
import backend.com.adquisiciones.domain.model.OrdenCompra;
import backend.com.adquisiciones.domain.ports.OrdenCompraRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class RecepcionarOCUseCase {

    private final OrdenCompraRepository ocRepository;
    private final ApplicationEventPublisher eventPublisher;

    @Transactional
    public OCResponse ejecutar(RecepcionarOCCommand command) {
        OrdenCompra oc = ocRepository.findById(command.getOrdenCompraId())
                .orElseThrow(() -> new IllegalArgumentException("Orden de Compra no encontrada con ID: " + command.getOrdenCompraId()));

        oc.recepcionar();

        OrdenCompra ocGuardada = ocRepository.save(oc);

        ocGuardada.getDomainEvents().forEach(eventPublisher::publishEvent);
        ocGuardada.clearDomainEvents();

        return OCResponse.fromDomain(ocGuardada);
    }
}


