package backend.com.produccion.application.usecase;

import backend.com.comercial.domain.model.EvaluacionNegocio;
import backend.com.comercial.domain.model.NotaVenta;
import backend.com.comercial.domain.ports.EvaluacionNegocioRepository;
import backend.com.produccion.domain.model.Costeo;
import backend.com.produccion.domain.model.OrdenProduccion;
import backend.com.produccion.domain.ports.CosteoRepository;
import backend.com.produccion.domain.ports.OrdenProduccionRepository;
import backend.com.produccion.service.CrearOrdenProduccionUseCase;
import backend.com.shared.exception.EntityNotFoundException;
import backend.com.shared.exception.ValidationException;
import backend.com.shared.valueobjects.DocumentNumber;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CrearOrdenProduccionUseCaseTest {

    @Mock
    private OrdenProduccionRepository repository;

    @Mock
    private EvaluacionNegocioRepository evnRepository;

    @Mock
    private CosteoRepository costeoRepository;

    @InjectMocks
    private CrearOrdenProduccionUseCase useCase;

    @Test
    @DisplayName("Debe crear una OP heredando el número del Costeo")
    void debeCrearOPHeredandoNumeroCosteo() {
        // Arrange
        DocumentNumber numeroCosteo = new DocumentNumber("COST-12345");
        DocumentNumber numeroNV = new DocumentNumber("NV-2000");

        NotaVenta nv = mock(NotaVenta.class);
        when(nv.getIdNV()).thenReturn(1L);
        when(nv.getNumeroNV()).thenReturn(numeroNV);
        when(nv.getEvaluacionNegocioId()).thenReturn(10L);
        when(nv.getFechaEntregaEstimada()).thenReturn(LocalDate.now());

        EvaluacionNegocio evn = mock(EvaluacionNegocio.class);
        when(evn.getCosteoId()).thenReturn(20L);

        Costeo costeo = mock(Costeo.class);
        when(costeo.getNumeroCosteo()).thenReturn(numeroCosteo);

        when(evnRepository.findById(10L)).thenReturn(Optional.of(evn));
        when(costeoRepository.findById(20L)).thenReturn(Optional.of(costeo));
        when(repository.save(any(OrdenProduccion.class))).thenAnswer(i -> i.getArguments()[0]);

        // Act
        OrdenProduccion result = useCase.execute(nv);

        // Assert
        assertThat(result.getNumeroOP()).isEqualTo(numeroCosteo);
        verify(repository).save(argThat(op -> op.getNumeroOP().equals(numeroCosteo)));
    }

    @Test
    @DisplayName("Debe lanzar ValidationException si la Nota de Venta es nula")
    void debeLanzarExcepcionSiNVEsNula() {
        assertThatThrownBy(() -> useCase.execute(null))
                .isInstanceOf(ValidationException.class)
                .hasMessageContaining("La Nota de Venta no puede ser nula");
    }

    @Test
    @DisplayName("Debe lanzar EntityNotFoundException si la EVN no existe")
    void debeLanzarExcepcionSiEvnNoExiste() {
        // Arrange
        NotaVenta nv = mock(NotaVenta.class);
        when(nv.getEvaluacionNegocioId()).thenReturn(999L);
        when(evnRepository.findById(999L)).thenReturn(Optional.empty());

        // Act & Assert
        assertThatThrownBy(() -> useCase.execute(nv))
                .isInstanceOf(EntityNotFoundException.class)
                .hasMessageContaining("Evaluación de Negocio no encontrada");
    }
}
