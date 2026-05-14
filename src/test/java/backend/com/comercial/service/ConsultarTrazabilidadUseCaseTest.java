package backend.com.comercial.service;

import backend.com.adquisiciones.domain.ports.SolicitudCompraRepository;
import backend.com.comercial.domain.model.EvaluacionNegocio;
import backend.com.comercial.domain.model.NotaVenta;
import backend.com.comercial.domain.model.SolicitudCostos;
import backend.com.comercial.domain.ports.EvaluacionNegocioRepository;
import backend.com.comercial.domain.ports.NotaVentaRepository;
import backend.com.comercial.domain.ports.SolicitudCostosRepository;
import backend.com.produccion.domain.model.Costeo;
import backend.com.produccion.domain.ports.CosteoRepository;
import backend.com.produccion.domain.ports.OrdenProduccionRepository;
import backend.com.shared.dto.DocumentTraceDTO;
import backend.com.shared.exception.EntityNotFoundException;
import backend.com.shared.valueobjects.DocumentNumber;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ConsultarTrazabilidadUseCaseTest {

    @Mock
    private NotaVentaRepository nvRepository;
    @Mock
    private EvaluacionNegocioRepository evnRepository;
    @Mock
    private CosteoRepository costeoRepository;
    @Mock
    private SolicitudCostosRepository scosRepository;
    @Mock
    private OrdenProduccionRepository opRepository;
    @Mock
    private SolicitudCompraRepository scRepository;
    @Mock
    private backend.com.produccion.domain.ports.OrdenTrabajoRepository otRepository;

    @InjectMocks
    private ConsultarTrazabilidadUseCase useCase;

    @Test
    @DisplayName("Debe retornar la cadena completa de documentos vinculados")
    void debeRetornarCadenaCompleta() {
        // Arrange
        Long nvId = 1L;
        NotaVenta nv = mock(NotaVenta.class);
        when(nv.getIdNV()).thenReturn(nvId);
        when(nv.getNumeroNV()).thenReturn(new DocumentNumber(100L));
        when(nv.getEvaluacionNegocioId()).thenReturn(10L);
        when(nv.getEstado()).thenReturn(backend.com.comercial.domain.model.EstadoNV.BORRADOR);

        EvaluacionNegocio evn = mock(EvaluacionNegocio.class);
        when(evn.getEvaluacionNegocioId()).thenReturn(10L);
        when(evn.getNumeroEvn()).thenReturn(new DocumentNumber(90L));
        when(evn.getCosteoId()).thenReturn(20L);
        when(evn.getEstado()).thenReturn(backend.com.comercial.domain.model.EstadoEVN.APROBADA);

        Costeo costeo = mock(Costeo.class);
        when(costeo.getIdCosteo()).thenReturn(20L);
        when(costeo.getNumeroCosteo()).thenReturn(new DocumentNumber(80L));
        when(costeo.getSolicitudCostosId()).thenReturn(30L);

        SolicitudCostos scos = mock(SolicitudCostos.class);
        when(scos.getIdSCOS()).thenReturn(30L);
        when(scos.getNumeroSCOS()).thenReturn(new DocumentNumber(70L));
        when(scos.getEstado()).thenReturn("APROBADA");

        when(nvRepository.findById(nvId)).thenReturn(Optional.of(nv));
        when(evnRepository.findById(10L)).thenReturn(Optional.of(evn));
        when(costeoRepository.findById(20L)).thenReturn(Optional.of(costeo));
        when(scosRepository.findById(30L)).thenReturn(Optional.of(scos));

        // Act
        List<DocumentTraceDTO> result = useCase.ejecutar(nvId);

        // Assert
        assertThat(result).hasSizeGreaterThanOrEqualTo(4);
        assertThat(result).extracting(DocumentTraceDTO::getTipoDocumento)
                .contains("Nota Venta", "Evaluación Negocio", "Costeo", "Solicitud Costos");
    }

    @Test
    @DisplayName("Debe lanzar EntityNotFoundException si la NV no existe")
    void debeLanzarExcepcionSiNvNoExiste() {
        when(nvRepository.findById(999L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> useCase.ejecutar(999L))
                .isInstanceOf(EntityNotFoundException.class);
    }
}
