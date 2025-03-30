package com.DriveZone.DriveZone;

import com.DriveZone.DriveZone.models.Factura;
import com.DriveZone.DriveZone.models.OrdenCompra;
import com.DriveZone.DriveZone.repository.FacturaRepository;
import com.DriveZone.DriveZone.repository.OrdenCompraRepository;
import com.DriveZone.DriveZone.services.FacturaService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Collections;
import java.util.Date;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;


public class FacturaServiceTest {
    @Mock
    private FacturaRepository facturaRepository;

    @Mock
    private OrdenCompraRepository ordenCompraRepository;

    @InjectMocks
    private FacturaService facturaService;

    private Factura factura;
    private OrdenCompra ordenCompra;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        // Datos de prueba
        ordenCompra = new OrdenCompra();
        ordenCompra.setIdOrdenCompra(1);

        factura = new Factura();
        factura.setIdFactura(1);
        factura.setEmpresaNombre("Empresa Test");
        factura.setNit("123456789");
        factura.setDireccion("Calle Falsa 123");
        factura.setMetodoPago("Tarjeta");
        factura.setFecha(new Date());
        factura.setSubtotal(100.00);
        factura.setDescuento(10.00);
        factura.setImpuestos(15.00);
        factura.setTotal(105.00);
        factura.setOrdenCompra(ordenCompra);
    }

    @Test
    void generarFactura_CuandoOrdenCompraExiste_DeberiaGuardarFactura() {
        when(ordenCompraRepository.findById(1)).thenReturn(Optional.of(ordenCompra));
        when(facturaRepository.save(any(Factura.class))).thenReturn(factura);

        Factura result = facturaService.generarFactura(factura, 1);

        assertNotNull(result);
        assertEquals("Empresa Test", result.getEmpresaNombre());
        verify(facturaRepository, times(1)).save(any(Factura.class));
    }

    @Test
    void generarFactura_CuandoOrdenCompraNoExiste_DeberiaLanzarExcepcion() {
        when(ordenCompraRepository.findById(2)).thenReturn(Optional.empty());

        Exception exception = assertThrows(RuntimeException.class, () -> {
            facturaService.generarFactura(factura, 2);
        });

        assertEquals("Orden de compra no encontrada.", exception.getMessage());
    }

    @Test
    void obtenerTodas_DeberiaRetornarListaDeFacturas() {
        when(facturaRepository.findAll()).thenReturn(Collections.singletonList(factura));

        assertEquals(1, facturaService.obtenerTodas().size());
        verify(facturaRepository, times(1)).findAll();
    }

    @Test
    void obtenerPorId_CuandoExiste_DeberiaRetornarFactura() {
        when(facturaRepository.findById(1)).thenReturn(Optional.of(factura));

        Optional<Factura> result = facturaService.obtenerPorId(1);

        assertTrue(result.isPresent());
        assertEquals("Empresa Test", result.get().getEmpresaNombre());
    }

    @Test
    void obtenerPorId_CuandoNoExiste_DeberiaRetornarVacio() {
        when(facturaRepository.findById(2)).thenReturn(Optional.empty());

        Optional<Factura> result = facturaService.obtenerPorId(2);

        assertFalse(result.isPresent());
    }

    @Test
    void guardarFactura_DeberiaGuardarCorrectamente() {
        when(facturaRepository.save(any(Factura.class))).thenReturn(factura);

        Factura result = facturaService.guardarFactura(factura);

        assertNotNull(result);
        verify(facturaRepository, times(1)).save(factura);
    }

    @Test
    void eliminarFactura_DeberiaEliminarCorrectamente() {
        doNothing().when(facturaRepository).deleteById(1);

        facturaService.eliminarFactura(1);

        verify(facturaRepository, times(1)).deleteById(1);
    }

}
