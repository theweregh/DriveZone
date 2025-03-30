package com.DriveZone.DriveZone;

import com.DriveZone.DriveZone.models.Factura;
import com.DriveZone.DriveZone.repository.FacturaRepository;
import com.DriveZone.DriveZone.services.FacturaService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.io.IOException;
import java.util.Date;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

public class FacturaServiceTest {
    @Mock
    private FacturaRepository facturaRepository;

    @InjectMocks
    private FacturaService facturaService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testGenerarFacturaPDF_FacturaExiste() throws IOException {
        // Arrange: Crear una factura simulada
        Factura factura = new Factura();
        factura.setIdFactura(1);
        factura.setEmpresaNombre("DriveZone");
        factura.setDireccion("Av. Principal 123");
        factura.setMetodoPago("Tarjeta");
        factura.setSubtotal(100.00);
        factura.setDescuento(5.00);
        factura.setImpuestos(10.00);
        factura.setTotal(105.00);

        // Asegurar que la fecha no sea null
        factura.setFecha(new Date());

        when(facturaRepository.findById(1)).thenReturn(Optional.of(factura));

        // Act: Generar el PDF
        byte[] pdfBytes = facturaService.generarFacturaPDF(1);

        // Assert: Validaciones
        assertNotNull(pdfBytes, "El PDF no debe ser nulo");
        assertTrue(pdfBytes.length > 0, "El PDF debe contener datos");
    }

    @Test
    void testGenerarFacturaPDF_FacturaNoExiste() {
        // Arrange: Simular que la factura no existe
        when(facturaRepository.findById(99)).thenReturn(Optional.empty());

        // Act & Assert: Se espera una excepción
        Exception exception = assertThrows(RuntimeException.class, () -> facturaService.generarFacturaPDF(99));
        assertEquals("Factura no encontrada", exception.getMessage());
    }
}
