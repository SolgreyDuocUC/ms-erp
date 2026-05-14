package backend.com.adquisiciones.domain.model;

import backend.com.shared.valueobjects.DocumentNumber;
import backend.com.shared.valueobjects.Money;
import backend.com.shared.valueobjects.TenantId;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class OrdenCompraTest {

    @Test
    @DisplayName("Debe crear una Orden de Compra correctamente y registrar evento")
    void debeCrearOrdenCompra() {
        // Arrange
        DocumentNumber numero = new DocumentNumber(123L);
        TenantId tenantId = new TenantId(1L);
        ItemOC item = new ItemOC(null, null, null, "Descripción", 10, new Money(new BigDecimal("100"), "CLP"));

        // Act
        OrdenCompra oc = OrdenCompra.crear(numero, tenantId, 1L, null, 2L, TipoOC.PROVEEDOR, List.of(item));

        // Assert
        assertThat(oc.getNumeroOC()).isEqualTo(numero);
        assertThat(oc.getEstado()).isEqualTo(EstadoOC.EMITIDA);
        assertThat(oc.getMontoTotal().getAmount()).isEqualByComparingTo(new BigDecimal("1000")); // 10 * 100
        assertThat(oc.getDomainEvents()).hasSize(1);
        assertThat(oc.getDomainEvents().get(0).getClass().getSimpleName()).isEqualTo("OCEmitidaEvent");
    }

    @Test
    @DisplayName("Debe lanzar excepción si se intenta crear sin items")
    void debeLanzarExcepcionAlCrearSinItems() {
        // Arrange
        DocumentNumber numero = new DocumentNumber(123L);
        TenantId tenantId = new TenantId(1L);

        // Act & Assert
        assertThatThrownBy(() -> OrdenCompra.crear(numero, tenantId, 1L, null, 2L, TipoOC.PROVEEDOR, List.of()))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("La Orden de Compra debe tener al menos un item");
    }

    @Test
    @DisplayName("Debe cambiar estado a RECIBIDA y registrar evento")
    void debeRecepcionarOrdenCompra() {
        // Arrange
        DocumentNumber numero = new DocumentNumber(123L);
        TenantId tenantId = new TenantId(1L);
        ItemOC item = new ItemOC(null, null, null, "Descripción", 10, new Money(new BigDecimal("100"), "CLP"));
        OrdenCompra oc = OrdenCompra.crear(numero, tenantId, 1L, null, 2L, TipoOC.PROVEEDOR, List.of(item));
        oc.clearDomainEvents(); // Setup inicial

        // Act
        oc.recepcionar();

        // Assert
        assertThat(oc.getEstado()).isEqualTo(EstadoOC.RECIBIDA);
        assertThat(oc.getFechaRecepcion()).isNotNull();
        assertThat(oc.getDomainEvents()).hasSize(1);
        assertThat(oc.getDomainEvents().get(0).getClass().getSimpleName()).isEqualTo("OCRecepcionadaEvent");
    }

    @Test
    @DisplayName("Debe lanzar excepción si se intenta recepcionar una OC que no está permitida")
    void debeLanzarExcepcionAlRecepcionarEstadoInvalido() {
        // Arrange
        OrdenCompra oc = new OrdenCompra(1L, new DocumentNumber(123L), new TenantId(1L), 1L, null, 2L, TipoOC.PROVEEDOR,
                EstadoOC.RECIBIDA, new Money(BigDecimal.ZERO, "CLP"), null, null, List.of());

        // Act & Assert
        assertThatThrownBy(oc::recepcionar)
                .isInstanceOf(IllegalStateException.class)
                .hasMessageContaining("La OC no está en estado válido para ser recepcionada");
    }
}
