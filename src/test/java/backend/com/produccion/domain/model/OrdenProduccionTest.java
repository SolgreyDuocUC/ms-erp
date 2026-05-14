package backend.com.produccion.domain.model;

import backend.com.shared.valueobjects.DocumentNumber;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class OrdenProduccionTest {

    @Test
    @DisplayName("Debe cambiar de PENDIENTE a EN_PROCESO al recepcionar")
    void debeCambiarEstadoAEnProcesoAlRecepcionar() {
        // Arrange
        OrdenProduccion op = OrdenProduccion.crearNueva(
                new DocumentNumber(5001L),
                1L,
                LocalDate.now().plusDays(10));

        // Act
        op.recepcionar();

        // Assert
        assertThat(op.getEstado()).isEqualTo(EstadoOP.EN_PROCESO);
        assertThat(op.getFechaInicio()).isNotNull();
    }

    @Test
    @DisplayName("No debe permitir recepcionar una OP que ya está en proceso")
    void noDebePermitirRecepcionarSiYaEstaEnProceso() {
        // Arrange
        OrdenProduccion op = OrdenProduccion.crearNueva(
                new DocumentNumber(5001L),
                1L,
                LocalDate.now());
        op.recepcionar();

        // Act & Assert
        assertThatThrownBy(op::recepcionar)
                .isInstanceOf(IllegalStateException.class)
                .hasMessageContaining("Solo las OPs pendientes pueden ser recepcionadas");
    }
}
