package backend.com.adquisiciones.domain.events;

import backend.com.shared.events.DomainEvent;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class OCRecepcionadaEvent extends DomainEvent {
    private final Long ordenCompraId;
    private final Long ordenProduccionId;
}
