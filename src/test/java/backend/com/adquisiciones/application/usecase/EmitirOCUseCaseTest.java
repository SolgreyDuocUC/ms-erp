package backend.com.adquisiciones.application.usecase;

import backend.com.adquisiciones.application.dto.EmitirOCCommand;
import backend.com.adquisiciones.application.dto.OCItemDTO;
import backend.com.adquisiciones.application.dto.OCResponse;
import backend.com.adquisiciones.domain.model.EstadoSC;
import backend.com.adquisiciones.domain.model.OrdenCompra;
import backend.com.adquisiciones.domain.model.SolicitudCompra;
import backend.com.adquisiciones.domain.model.TipoOC;
import backend.com.adquisiciones.domain.ports.OrdenCompraRepository;
import backend.com.adquisiciones.domain.ports.SolicitudCompraRepository;
import backend.com.shared.valueobjects.DocumentNumber;
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
class EmitirOCUseCaseTest {

    @Mock
    private OrdenCompraRepository ocRepository;

    @Mock
    private SolicitudCompraRepository scRepository;

    @Mock
    private ApplicationEventPublisher eventPublisher;

    @InjectMocks
    private EmitirOCUseCase useCase;

    @Test
    @DisplayName("Debe emitir orden de compra y publicar evento si SC está aprobada")
    @SuppressWarnings("null")
    void debeEmitirOrdenCompra() {
        // Arrange
        Long scId = 1L;
        SolicitudCompra sc = new SolicitudCompra(scId, new DocumentNumber(123L), new TenantId(1L), 2L,
                EstadoSC.APROBADA, null, List.of());
        when(scRepository.findById(scId)).thenReturn(Optional.of(sc));

        OCItemDTO itemDto = new OCItemDTO();
        itemDto.setProductoId(10L);
        itemDto.setDescripcionProducto("TELA MUESTRA");
        itemDto.setCantidad(5);
        itemDto.setPrecioUnitario(new BigDecimal("100"));
        itemDto.setMoneda("CLP");

        EmitirOCCommand command = new EmitirOCCommand();
        command.setSolicitudCompraId(scId);
        command.setTenantId(1L);
        command.setProveedorId(100L);
        command.setTipo(TipoOC.PROVEEDOR);
        command.setItems(List.of(itemDto));

        when(ocRepository.save(any(OrdenCompra.class))).thenAnswer(invocation -> {
            OrdenCompra ocArg = invocation.getArgument(0);
            try {
                java.lang.reflect.Field idField = OrdenCompra.class.getDeclaredField("idOC");
                idField.setAccessible(true);
                idField.set(ocArg, 1L);
            } catch (Exception e) {
            }
            return ocArg;
        });

        // Act
        OCResponse response = useCase.ejecutar(command);

        // Assert
        assertThat(response.getIdOC()).isEqualTo(1L);
        assertThat(response.getEstado()).isEqualTo("EMITIDA");
        assertThat(response.getSolicitudCompraId()).isEqualTo(scId);

        verify(ocRepository).save(any(OrdenCompra.class));
        verify(eventPublisher, times(1))
                .publishEvent(any(backend.com.adquisiciones.domain.events.OCEmitidaEvent.class));
    }

    @Test
    @DisplayName("Debe fallar si SC no está aprobada")
    void debeFallarEmitirOCSiSCNoAprobada() {
        // Arrange
        Long scId = 1L;
        SolicitudCompra sc = new SolicitudCompra(scId, new DocumentNumber(123L), new TenantId(1L), 2L,
                EstadoSC.PENDIENTE, null, List.of());
        when(scRepository.findById(scId)).thenReturn(Optional.of(sc));

        EmitirOCCommand command = new EmitirOCCommand();
        command.setSolicitudCompraId(scId);

        // Act & Assert
        assertThatThrownBy(() -> useCase.ejecutar(command))
                .isInstanceOf(IllegalStateException.class)
                .hasMessageContaining("La Solicitud de Compra debe estar en estado APROBADA");
    }
}
