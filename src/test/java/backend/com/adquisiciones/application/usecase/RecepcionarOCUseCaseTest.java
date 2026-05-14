package backend.com.adquisiciones.application.usecase;

import backend.com.adquisiciones.application.dto.OCResponse;
import backend.com.adquisiciones.application.dto.RecepcionarOCCommand;
import backend.com.adquisiciones.domain.model.EstadoOC;
import backend.com.adquisiciones.domain.model.OrdenCompra;
import backend.com.adquisiciones.domain.model.TipoOC;
import backend.com.adquisiciones.domain.ports.OrdenCompraRepository;
import backend.com.shared.valueobjects.DocumentNumber;
import backend.com.shared.valueobjects.Money;
import backend.com.shared.valueobjects.TenantId;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.context.ApplicationEventPublisher;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class RecepcionarOCUseCaseTest {

    @Mock
    private OrdenCompraRepository ocRepository;

    @Mock
    private ApplicationEventPublisher eventPublisher;

    @InjectMocks
    private RecepcionarOCUseCase useCase;

    @Test
    @DisplayName("Debe recepcionar la OC y publicar el evento OCRecepcionadaEvent")
    @SuppressWarnings("null")
    void debeRecepcionarOC() {
        // Arrange
        OrdenCompra oc = new OrdenCompra(1L, new DocumentNumber(123L), new TenantId(1L), null, null,
                null, TipoOC.PROVEEDOR, EstadoOC.EMITIDA, new Money(BigDecimal.ZERO, "CLP"), null, null, List.of());
        when(ocRepository.findById(1L)).thenReturn(Optional.of(oc));

        when(ocRepository.save(any(OrdenCompra.class))).thenReturn(oc);

        RecepcionarOCCommand command = new RecepcionarOCCommand();
        command.setOrdenCompraId(1L);

        // Act
        OCResponse response = useCase.ejecutar(command);

        // Assert
        assertThat(response.getEstado()).isEqualTo("RECIBIDA");

        verify(ocRepository).save(any(OrdenCompra.class));
        verify(eventPublisher, times(1))
                .publishEvent(any(backend.com.adquisiciones.domain.events.OCRecepcionadaEvent.class));
    }

    @Test
    @DisplayName("Debe fallar si intenta recepcionar una OC que ya fue recepcionada")
    void debeFallarRecepcionarOCYaRecepcionada() {
        // Arrange
        OrdenCompra oc = new OrdenCompra(1L, new DocumentNumber(123L), new TenantId(1L), null, null,
                null, TipoOC.PROVEEDOR, EstadoOC.RECIBIDA, new Money(BigDecimal.ZERO, "CLP"), null, null, List.of());
        when(ocRepository.findById(1L)).thenReturn(Optional.of(oc));

        RecepcionarOCCommand command = new RecepcionarOCCommand();
        command.setOrdenCompraId(1L);

        // Act & Assert
        assertThatThrownBy(() -> useCase.ejecutar(command))
                .isInstanceOf(IllegalStateException.class)
                .hasMessageContaining("La OC no está en estado válido para ser recepcionada");

        verify(ocRepository, never()).save(any(OrdenCompra.class));
    }
}
