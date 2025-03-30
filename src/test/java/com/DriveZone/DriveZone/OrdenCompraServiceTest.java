package com.DriveZone.DriveZone;
import com.DriveZone.DriveZone.models.OrdenCompra;
import com.DriveZone.DriveZone.repository.OrdenCompraRepository;
import com.DriveZone.DriveZone.services.OrdenCompraService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
public class OrdenCompraServiceTest {
    @Mock
    private OrdenCompraRepository ordenCompraRepository;

    @InjectMocks
    private OrdenCompraService ordenCompraService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testObtenerTodasLasOrdenes() {
        // Datos simulados
        OrdenCompra orden1 = new OrdenCompra();
        OrdenCompra orden2 = new OrdenCompra();
        List<OrdenCompra> listaOrdenes = Arrays.asList(orden1, orden2);

        // Simular el comportamiento del repositorio
        when(ordenCompraRepository.findAll()).thenReturn(listaOrdenes);

        // Llamar al método
        List<OrdenCompra> resultado = ordenCompraService.obtenerTodasLasOrdenes();

        // Verificar resultados
        assertNotNull(resultado);
        assertEquals(2, resultado.size());
        verify(ordenCompraRepository, times(1)).findAll();
    }

    @Test
    void testObtenerOrdenPorId_Existente() {
        // Datos simulados
        OrdenCompra orden = new OrdenCompra();
        orden.setIdOrdenCompra(1);

        // Simular búsqueda por ID
        when(ordenCompraRepository.findById(1)).thenReturn(Optional.of(orden));

        // Llamar al método
        Optional<OrdenCompra> resultado = ordenCompraService.obtenerOrdenPorId(1);

        // Verificar resultados
        assertTrue(resultado.isPresent());
        assertEquals(1, resultado.get().getIdOrdenCompra());
        verify(ordenCompraRepository, times(1)).findById(1);
    }

    @Test
    void testObtenerOrdenPorId_NoExistente() {
        // Simular que no se encuentra la orden
        when(ordenCompraRepository.findById(99)).thenReturn(Optional.empty());

        // Llamar al método
        Optional<OrdenCompra> resultado = ordenCompraService.obtenerOrdenPorId(99);

        // Verificar que no se encontró nada
        assertFalse(resultado.isPresent());
        verify(ordenCompraRepository, times(1)).findById(99);
    }

    @Test
    void testCrearOrden() {
        // Datos simulados
        OrdenCompra nuevaOrden = new OrdenCompra();
        nuevaOrden.setIdOrdenCompra(5);

        // Simular el guardado en BD
        when(ordenCompraRepository.save(nuevaOrden)).thenReturn(nuevaOrden);

        // Llamar al método
        OrdenCompra resultado = ordenCompraService.crearOrden(nuevaOrden);

        // Verificar resultados
        assertNotNull(resultado);
        assertEquals(5, resultado.getIdOrdenCompra());
        verify(ordenCompraRepository, times(1)).save(nuevaOrden);
    }

    @Test
    void testEliminarOrden() {
        // Llamar al método
        ordenCompraService.eliminarOrden(3);

        // Verificar que se llamó al repositorio
        verify(ordenCompraRepository, times(1)).deleteById(3);
    }
}
