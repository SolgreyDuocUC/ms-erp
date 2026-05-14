package backend.com.adquisiciones.domain.model;

import backend.com.shared.valueobjects.DocumentNumber;
import backend.com.shared.valueobjects.TenantId;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class SolicitudCompraTest {

    @Test
    @DisplayName("Debe crear una Solicitud de Compra correctamente y registrar evento")
    void debeCrearSolicitudCompra() {
        // Arrange
        DocumentNumber numero = new DocumentNumber(123L);
        TenantId tenantId = new TenantId(1L);
        ItemSC item = new ItemSC(1L, null, null, "Descripción", 10, null, TipoItemSC.SC);

        // Act
        SolicitudCompra sc = SolicitudCompra.crear(numero, tenantId, 1L, List.of(item));

        // Assert
        assertThat(sc.getNumeroSC()).isEqualTo(numero);
        assertThat(sc.getEstado()).isEqualTo(EstadoSC.PENDIENTE);
        assertThat(sc.getDomainEvents()).hasSize(1);
        assertThat(sc.getDomainEvents().get(0).getClass().getSimpleName()).isEqualTo("SCCreadaEvent");
    }

    @Test
    @DisplayName("Debe lanzar excepción si se intenta crear sin items")
    void debeLanzarExcepcionAlCrearSinItems() {
        // Arrange
        DocumentNumber numero = new DocumentNumber(123L);
        TenantId tenantId = new TenantId(1L);

        // Act & Assert
        assertThatThrownBy(() -> SolicitudCompra.crear(numero, tenantId, 1L, List.of()))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("La Solicitud de Compra debe tener al menos un item");
    }

    @Test
    @DisplayName("Debe aprobar SC pendiente y registrar evento")
    void debeAprobarSolicitudCompra() {
        // Arrange
        DocumentNumber numero = new DocumentNumber(123L);
        TenantId tenantId = new TenantId(1L);
        ItemSC item = new ItemSC(1L, null, null, "Descripción", 10, null, TipoItemSC.SC);
        SolicitudCompra sc = SolicitudCompra.crear(numero, tenantId, 1L, List.of(item));
        sc.clearDomainEvents(); // Setup inicial

        // Act
        sc.aprobar();

        // Assert
        assertThat(sc.getEstado()).isEqualTo(EstadoSC.APROBADA);
        assertThat(sc.getDomainEvents()).hasSize(1);
        assertThat(sc.getDomainEvents().get(0).getClass().getSimpleName()).isEqualTo("SCAprobadaEvent");
    }

    @Test
    @DisplayName("Debe lanzar excepción si se intenta aprobar una SC no PENDIENTE")
    void debeLanzarExcepcionAlAprobarEstadoInvalido() {
        // Arrange
        SolicitudCompra sc = new SolicitudCompra(1L, new DocumentNumber(123L), new TenantId(1L), 1L, EstadoSC.APROBADA,
                null, List.of());

        // Act & Assert
        assertThatThrownBy(sc::aprobar)
                .isInstanceOf(IllegalStateException.class)
                .hasMessageContaining("Solo las Solicitudes Pendientes pueden ser aprobadas");
    }

    @Test
    @DisplayName("Debe permitir generar OC si está aprobada o parcial")
    void debePermitirGenerarOC() {
        // Arrange
        SolicitudCompra scParcial = new SolicitudCompra(1L, new DocumentNumber(123L), new TenantId(1L), 1L,
                EstadoSC.PARCIAL, null, List.of());
        SolicitudCompra scAprobada = new SolicitudCompra(1L, new DocumentNumber(123L), new TenantId(1L), 1L,
                EstadoSC.APROBADA, null, List.of());
        SolicitudCompra scPendiente = new SolicitudCompra(1L, new DocumentNumber(123L), new TenantId(1L), 1L,
                EstadoSC.PENDIENTE, null, List.of());

        // Act & Assert
        assertThat(scParcial.puedeGenerarOC()).isTrue();
        assertThat(scAprobada.puedeGenerarOC()).isTrue();
        assertThat(scPendiente.puedeGenerarOC()).isFalse();
    }
}
