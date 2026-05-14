package backend.com.adquisiciones.application.usecase;

import backend.com.adquisiciones.application.dto.CrearSCCommand;
import backend.com.adquisiciones.application.dto.SCItemDTO;
import backend.com.adquisiciones.application.dto.SCResponse;
import backend.com.adquisiciones.domain.model.SolicitudCompra;
import backend.com.adquisiciones.domain.ports.SolicitudCompraRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.context.ApplicationEventPublisher;

import java.math.BigDecimal;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CrearSCUseCaseTest {

    @Mock
    private SolicitudCompraRepository scRepository;

    @Mock
    private ApplicationEventPublisher eventPublisher;

    @InjectMocks
    private CrearSCUseCase useCase;

    @Test
    @DisplayName("Debe crear una Solicitud de Compra y publicar el evento de dominio")
    @SuppressWarnings("null")
    void debeCrearSC() {
        // Arrange
        SCItemDTO item = new SCItemDTO();
        item.setProductoId(1L);
        item.setDescripcionProducto("Tela");
        item.setCantidad(100);
        item.setPrecioEstimadoUnitario(new BigDecimal("10"));
        item.setMoneda("USD");

        CrearSCCommand command = new CrearSCCommand();
        command.setTenantId(1L);
        command.setNotaVentaId(2L);
        command.setItems(List.of(item));

        when(scRepository.save(any(SolicitudCompra.class))).thenAnswer(invocation -> {
            SolicitudCompra scArg = invocation.getArgument(0);
            try {
                java.lang.reflect.Field idField = SolicitudCompra.class.getDeclaredField("idSC");
                idField.setAccessible(true);
                idField.set(scArg, 1L);
            } catch (Exception e) {
            }
            return scArg;
        });

        // Act
        SCResponse response = useCase.ejecutar(command);

        // Assert
        assertThat(response.getIdSC()).isEqualTo(1L);
        assertThat(response.getEstado()).isEqualTo("PENDIENTE");
        assertThat(response.getNotaVentaId()).isEqualTo(2L);

        verify(scRepository).save(any(SolicitudCompra.class));
        verify(eventPublisher, times(1)).publishEvent(any(backend.com.adquisiciones.domain.events.SCCreadaEvent.class));
    }
}
