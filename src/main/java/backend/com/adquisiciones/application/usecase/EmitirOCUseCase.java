package backend.com.adquisiciones.application.usecase;

import backend.com.adquisiciones.application.dto.EmitirOCCommand;
import backend.com.adquisiciones.application.dto.OCResponse;
import backend.com.adquisiciones.domain.model.ItemOC;
import backend.com.adquisiciones.domain.model.OrdenCompra;
import backend.com.adquisiciones.domain.model.SolicitudCompra;
import backend.com.adquisiciones.domain.ports.OrdenCompraRepository;
import backend.com.adquisiciones.domain.ports.SolicitudCompraRepository;
import backend.com.shared.valueobjects.DocumentNumber;
import backend.com.shared.valueobjects.Money;
import backend.com.shared.valueobjects.TenantId;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class EmitirOCUseCase {

        private final OrdenCompraRepository ocRepository;
        private final SolicitudCompraRepository scRepository;
        private final ApplicationEventPublisher eventPublisher;

        @Transactional
        public OCResponse ejecutar(EmitirOCCommand command) {
                SolicitudCompra sc = scRepository.findById(command.getSolicitudCompraId())
                                .orElseThrow(() -> new IllegalArgumentException(
                                                "Solicitud de Compra no encontrada con ID: "
                                                                + command.getSolicitudCompraId()));

                if (!sc.puedeGenerarOC()) {
                        throw new IllegalStateException(
                                        "La Solicitud de Compra debe estar en estado APROBADA para emitir una OC");
                }

                Long numeroGenerado = Long
                                .parseLong(LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyMMddHHmmss")));
                DocumentNumber numero = new DocumentNumber(numeroGenerado);

                List<ItemOC> items = command.getItems().stream().map(dto -> new ItemOC(
                                dto.getIdOCItem(),
                                dto.getNroItem(),
                                dto.getProductoId(),
                                dto.getDescripcionProducto(),
                                dto.getCantidad(),
                                new Money(dto.getPrecioUnitario(), dto.getMoneda() != null ? dto.getMoneda() : "CLP")))
                                .collect(Collectors.toList());

                OrdenCompra oc = OrdenCompra.crear(
                                numero,
                                new TenantId(command.getTenantId()),
                                sc.getIdSC(),
                                command.getOrdenProduccionId(),
                                command.getProveedorId(),
                                command.getTipo(),
                                items);

                OrdenCompra ocGuardada = ocRepository.save(oc);

                ocGuardada.getDomainEvents().forEach(eventPublisher::publishEvent);
                ocGuardada.clearDomainEvents();

                return OCResponse.fromDomain(ocGuardada);
        }
}
