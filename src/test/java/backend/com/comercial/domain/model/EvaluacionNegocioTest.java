package backend.com.comercial.domain.model;

import backend.com.shared.exception.EVNBusinessException;
import backend.com.shared.valueobjects.DocumentNumber;
import backend.com.shared.valueobjects.Money;
import org.junit.jupiter.api.Test;
import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;

class EvaluacionNegocioTest {

    @Test
    void debeCalcularCorrectamenteElMargenConComision() {
        // Given
        EvaluacionNegocio evn = EvaluacionNegocio.crear(
                new DocumentNumber(100L), 1L, 1L, 1L, 1L, new BigDecimal("10.00"), // 10% comisión
                null, null, null
        );

        // Item: Precio 1000, Costo 600, Cantidad 10 -> Venta 10000, Costo 6000
        ItemEVN item = new ItemEVN(1L, 1L, 10, new Money(new BigDecimal("1000"), "CLP"),
                new Money(new BigDecimal("600"), "CLP"),
                new BigDecimal("600"), BigDecimal.ZERO, BigDecimal.ZERO,
                "SC", null);
        evn.addItem(item);

        // When
        BigDecimal montoTotal = evn.getMontoTotal().getAmount(); // 10000
        BigDecimal costoTotal = evn.getCostoTotal().getAmount(); // 6000
        BigDecimal montoComision = evn.getMontoComision().getAmount(); // 1000 (10% de 10000)
        BigDecimal margen = evn.getMargenGanancia(); // 10000 - 6000 - 1000 = 3000
        BigDecimal rentabilidad = evn.getRentabilidadEsperada(); // (3000 / 10000) * 100 = 30%

        // Then
        assertEquals(new BigDecimal("10000.00").setScale(2), montoTotal.setScale(2));
        assertEquals(new BigDecimal("6000.00").setScale(2), costoTotal.setScale(2));
        assertEquals(new BigDecimal("1000.00").setScale(2), montoComision.setScale(2));
        assertEquals(new BigDecimal("3000.00").setScale(2), margen.setScale(2));
        assertEquals(new BigDecimal("30.0000").setScale(4), rentabilidad.setScale(4));
    }

    @Test
    void debeLanzarExcepcionAlAprobarSinItems() {
        EvaluacionNegocio evn = EvaluacionNegocio.crear(new DocumentNumber(100L), 1L, 1L, 1L, 1L, BigDecimal.ZERO, null, null, null);
        assertThrows(EVNBusinessException.class, evn::aprobar);
    }
}
