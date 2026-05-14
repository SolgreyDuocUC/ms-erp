package backend.com.comercial.domain.model;

import backend.com.shared.valueobjects.DocumentNumber;
import backend.com.shared.valueobjects.Money;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.LocalDate;

import static org.assertj.core.api.Assertions.assertThat;

class NotaVentaTest {

        @Test
        @DisplayName("Debe calcular los totales correctamente al agregar ítems")
        void debeCalcularTotalesCorrectamente() {
                // Arrange
                NotaVenta nv = NotaVenta.crear(
                                new DocumentNumber(1001L),
                                1L, 1L, 1L, false, null,
                                LocalDate.now().plusDays(7));

                ItemNV item1 = new ItemNV(1, 1L, "Polera", null, null, "Azul", "M", "Masculino", "P1", null, "N/A",
                                "OP", false,
                                null,
                                null, 10, new Money(new BigDecimal("5000"), "CLP"), null);
                ItemNV item2 = new ItemNV(2, 2L, "Pantalon", null, null, "Gris", "42", "Masculino", "P2", null, "N/A",
                                "OP",
                                false,
                                null, null, 5, new Money(new BigDecimal("12000"), "CLP"), null);

                // Act
                nv.addItem(item1); // 10 * 5000 = 50.000
                nv.addItem(item2); // 5 * 12000 = 60.000

                // Assert
                // Subtotal = 110.000
                // IVA (19%) = 20.900
                // Total = 130.900
                assertThat(nv.getMontoSubtotal().getAmount()).isEqualByComparingTo("110000");
                assertThat(nv.getMontoIva().getAmount()).isEqualByComparingTo("20900");
                assertThat(nv.getMontoTotal().getAmount()).isEqualByComparingTo("130900");
        }

        @Test
        @DisplayName("Debe cambiar de estado a APROBADA correctamente")
        void debeCambiarEstadoAAprobada() {
                // Arrange
                NotaVenta nv = NotaVenta.crear(
                                new DocumentNumber(1001L),
                                1L, 1L, 1L, false, null,
                                LocalDate.now());

                // Act
                nv.aprobar();

                // Assert
                assertThat(nv.getEstado()).isEqualTo(EstadoNV.APROBADA);
        }
}
