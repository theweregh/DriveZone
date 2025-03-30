package com.DriveZone.DriveZone;
import com.DriveZone.DriveZone.models.Accesorio;
import com.DriveZone.DriveZone.models.AccesorioHasOrdenCompra;
import com.DriveZone.DriveZone.models.AccesorioHasOrdenCompraId;
import com.DriveZone.DriveZone.models.OrdenCompra;
import com.DriveZone.DriveZone.repository.AccesorioOrdenCompraRepository;
import com.DriveZone.DriveZone.services.AccesorioOrdenCompraService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
@ExtendWith(MockitoExtension.class)
public class AccesorioOrdenCompraServiceTest {
    @Mock
    private AccesorioOrdenCompraRepository accesorioOrdenCompraRepository;

    @InjectMocks
    private AccesorioOrdenCompraService accesorioOrdenCompraService;

    private Accesorio accesorio;
    private OrdenCompra ordenCompra;
    private AccesorioHasOrdenCompra asociacion;

    @BeforeEach
    void setUp() {
        accesorio = new Accesorio();
        accesorio.setId(1);

        ordenCompra = new OrdenCompra();
        ordenCompra.setIdOrdenCompra(100);

        asociacion = new AccesorioHasOrdenCompra();
        asociacion.setAccesorio(accesorio);
        asociacion.setOrdenCompra(ordenCompra);
    }

    @Test
    void agregarAccesorioAOrden_Exitoso() {
        // Crear ID compuesto esperado
        AccesorioHasOrdenCompraId idEsperado = new AccesorioHasOrdenCompraId();
        idEsperado.setIdAccesorio(accesorio.getId());
        idEsperado.setIdOrdenCompra(ordenCompra.getIdOrdenCompra());

        asociacion.setId(idEsperado);

        // Simular el repositorio
        when(accesorioOrdenCompraRepository.save(any(AccesorioHasOrdenCompra.class))).thenReturn(asociacion);

        // Ejecutar método
        AccesorioHasOrdenCompra resultado = accesorioOrdenCompraService.agregarAccesorioAOrden(asociacion);

        // Verificaciones
        assertNotNull(resultado);
        assertEquals(idEsperado, resultado.getId());
        verify(accesorioOrdenCompraRepository, times(1)).save(asociacion);
    }

    @Test
    void agregarAccesorioAOrden_FallaPorDatosNulos() {
        AccesorioHasOrdenCompra asociacionInvalida = new AccesorioHasOrdenCompra();

        // Verificar que lanza excepción
        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            accesorioOrdenCompraService.agregarAccesorioAOrden(asociacionInvalida);
        });

        assertEquals("OrdenCompra o Accesorio no pueden ser nulos", exception.getMessage());
        verify(accesorioOrdenCompraRepository, never()).save(any());
    }

    @Test
    void obtenerAccesoriosDeOrden_Exitoso() {
        // Simular respuesta del repositorio
        List<AccesorioHasOrdenCompra> listaEsperada = Arrays.asList(asociacion);
        when(accesorioOrdenCompraRepository.findById_IdOrdenCompra(100)).thenReturn(listaEsperada);

        // Ejecutar método
        List<AccesorioHasOrdenCompra> resultado = accesorioOrdenCompraService.obtenerAccesoriosDeOrden(100);

        // Verificaciones
        assertNotNull(resultado);
        assertEquals(1, resultado.size());
        assertEquals(asociacion, resultado.get(0));
        verify(accesorioOrdenCompraRepository, times(1)).findById_IdOrdenCompra(100);
    }

    @Test
    void obtenerAccesoriosDeOrden_SinResultados() {
        // Simular lista vacía
        when(accesorioOrdenCompraRepository.findById_IdOrdenCompra(200)).thenReturn(Arrays.asList());

        // Ejecutar método
        List<AccesorioHasOrdenCompra> resultado = accesorioOrdenCompraService.obtenerAccesoriosDeOrden(200);

        // Verificaciones
        assertNotNull(resultado);
        assertTrue(resultado.isEmpty());
        verify(accesorioOrdenCompraRepository, times(1)).findById_IdOrdenCompra(200);
    }
}
