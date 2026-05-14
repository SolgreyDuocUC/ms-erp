package backend.com.adquisiciones.application.usecase;

import backend.com.adquisiciones.application.dto.CrearSCCommand;
import backend.com.adquisiciones.application.dto.SCResponse;
import backend.com.adquisiciones.domain.model.ItemSC;
import backend.com.adquisiciones.domain.model.SolicitudCompra;
import backend.com.adquisiciones.domain.model.TipoItemSC;
import backend.com.adquisiciones.domain.ports.SolicitudCompraRepository;
import backend.com.shared.valueobjects.DocumentNumber;
import backend.com.shared.valueobjects.Money;
import backend.com.shared.valueobjects.TenantId;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CrearSCUseCase {

    private final SolicitudCompraRepository scRepository;
    private final ApplicationEventPublisher eventPublisher;

    @Transactional
    public SCResponse ejecutar(CrearSCCommand command) {
        Long numeroVal = scRepository.findMaxNumero().orElse(0L) + 1;
        DocumentNumber numero = new DocumentNumber(numeroVal);

        List<ItemSC> items = new java.util.ArrayList<>();
        for (int i = 0; i < command.getItems().size(); i++) {
            backend.com.adquisiciones.application.dto.SCItemDTO dto = command.getItems().get(i);
            Long nroItem = dto.getNroItem();
            if (nroItem == null) {
                nroItem = (long) (i + 1);
            }
            items.add(new ItemSC(
                    dto.getIdSCItem(),
                    nroItem,
                    dto.getProductoId(),
                    dto.getDescripcionProducto(),
                    dto.getCantidad(),
                    new Money(dto.getPrecioEstimadoUnitario(), dto.getMoneda() != null ? dto.getMoneda() : "CLP"),
                    dto.getTipo() != null ? TipoItemSC.valueOf(dto.getTipo()) : TipoItemSC.SC));
        }

        SolicitudCompra sc = SolicitudCompra.crear(
                numero,
                new TenantId(command.getTenantId()),
                command.getNotaVentaId(),
                items);

        SolicitudCompra scGuardada = scRepository.save(sc);

        scGuardada.getDomainEvents().forEach(eventPublisher::publishEvent);
        scGuardada.clearDomainEvents();

        return SCResponse.fromDomain(scGuardada);
    }
}
