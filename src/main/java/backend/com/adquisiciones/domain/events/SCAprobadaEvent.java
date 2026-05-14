package backend.com.adquisiciones.domain.events;

import backend.com.shared.events.DomainEvent;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class SCAprobadaEvent extends DomainEvent {
    private final Long solicitudCompraId;
}
