package com.DriveZone.DriveZone;
import com.DriveZone.DriveZone.models.Accion;
import com.DriveZone.DriveZone.models.HistorialAccesorio;
import com.DriveZone.DriveZone.repository.HistorialAccesorioRepository;
import com.DriveZone.DriveZone.services.HistorialAccesorioService;
import com.itextpdf.text.DocumentException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.io.IOException;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;
@ExtendWith(MockitoExtension.class)
public class HistorialAccesorioServiceTest {
    @Mock
    private HistorialAccesorioRepository historialAccesorioRepository;

    @InjectMocks
    private HistorialAccesorioService historialAccesorioService;

    private List<HistorialAccesorio> historial;

    @BeforeEach
    void setUp() {
        // Crear objetos de prueba con datos de ejemplo
        HistorialAccesorio accesorio1 = new HistorialAccesorio(Accion.AGREGADO, "Llantas", 1);
        HistorialAccesorio accesorio2 = new HistorialAccesorio(Accion.ELIMINADO, "Espejos", 2);
        historial = Arrays.asList(accesorio1, accesorio2);
    }

    @Test
    void testObtenerHistorial() {
        // Simular el comportamiento del repositorio
        when(historialAccesorioRepository.findAll()).thenReturn(historial);

        // Llamar al método
        List<HistorialAccesorio> resultado = historialAccesorioService.obtenerHistorial();

        // Verificar el resultado
        assertNotNull(resultado);
        assertEquals(2, resultado.size());
    }

    @Test
    void testGenerarPdfHistorial() throws IOException, DocumentException {
        when(historialAccesorioRepository.findAll()).thenReturn(historial);

        byte[] pdfBytes = historialAccesorioService.generarPdfHistorial();

        assertNotNull(pdfBytes);
        assertTrue(pdfBytes.length > 0);
    }

    @Test
    void testGenerarPdfAgregados() throws IOException, DocumentException {
        when(historialAccesorioRepository.findAll()).thenReturn(historial);

        byte[] pdfBytes = historialAccesorioService.generarPdfAgregados();

        assertNotNull(pdfBytes);
        assertTrue(pdfBytes.length > 0);
    }

    @Test
    void testGenerarPdfEliminados() throws IOException, DocumentException {
        when(historialAccesorioRepository.findAll()).thenReturn(historial);

        byte[] pdfBytes = historialAccesorioService.generarPdfEliminados();

        assertNotNull(pdfBytes);
        assertTrue(pdfBytes.length > 0);
    }

    @Test
    void testGenerarPdfConListaVacia() throws IOException, DocumentException {
        when(historialAccesorioRepository.findAll()).thenReturn(Collections.emptyList());

        byte[] pdfBytes = historialAccesorioService.generarPdfHistorial();

        assertNotNull(pdfBytes);
        assertTrue(pdfBytes.length > 0); // El PDF sigue generándose aunque esté vacío
    }
}
