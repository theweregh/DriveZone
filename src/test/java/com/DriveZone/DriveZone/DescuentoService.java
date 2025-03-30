package com.DriveZone.DriveZone;

import com.DriveZone.DriveZone.models.Accesorio;
import com.DriveZone.DriveZone.repository.AccesorioRepository;
import com.DriveZone.DriveZone.services.AccesorioService;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class DescuentoService {

    @Mock
    private AccesorioRepository accesorioRepository;

    @InjectMocks
    private AccesorioService accesorioService;

    private Accesorio accesorio;

    @BeforeEach
    void setUp() {
        accesorio = new Accesorio();
        accesorio.setId(1);
        accesorio.setNombre("Volante Deportivo");
        accesorio.setPrecioVenta(100.0);
        accesorio.setDescuento(10.0); // 10% de descuento
    }

    @Test
    void testAplicarDescuentoCorrectamente() {
        when(accesorioRepository.findById(1)).thenReturn(Optional.of(accesorio));

        Optional<Accesorio> accesorioOptional = accesorioService.getAccesorioById(1);
        assertTrue(accesorioOptional.isPresent());
        Accesorio result = accesorioOptional.get();
        
        double precioFinal = result.getPrecioVenta() * (1 - result.getDescuento() / 100);
        assertEquals(90.0, precioFinal, 0.01);
    }

    @Test
    void testDescuentoEnLimites() {
        accesorio.setDescuento(0);
        assertEquals(100.0, accesorio.getPrecioVenta() * (1 - accesorio.getDescuento() / 100), 0.01);

        accesorio.setDescuento(100);
        assertEquals(0.0, accesorio.getPrecioVenta() * (1 - accesorio.getDescuento() / 100), 0.01);
    }

  
}
