package com.DriveZone.DriveZone;

import com.DriveZone.DriveZone.models.Accesorio;
import com.DriveZone.DriveZone.models.HistorialAccesorio;
import com.DriveZone.DriveZone.repository.AccesorioRepository;
import com.DriveZone.DriveZone.repository.HistorialAccesorioRepository;
import com.DriveZone.DriveZone.services.AccesorioService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class AccesorioServiceTest {
    @Mock
    private AccesorioRepository accesorioRepository;

    @Mock
    private HistorialAccesorioRepository historialRepository;

    @InjectMocks
    private AccesorioService accesorioService;

    private Accesorio accesorio;

    @BeforeEach
    void setUp() {
        accesorio = new Accesorio();
        accesorio.setId(1);
        accesorio.setNombre("Volante Logitech G29");
    }

    @Test
    void testGetAllAccesorios() {
        List<Accesorio> accesorios = Collections.singletonList(accesorio);
        when(accesorioRepository.findAll()).thenReturn(accesorios);

        List<Accesorio> result = accesorioService.getAllAccesorios();

        assertEquals(1, result.size());
        assertEquals("Volante Logitech G29", result.get(0).getNombre());
        verify(accesorioRepository, times(1)).findAll();
    }

    @Test
    void testGetAccesorioById_Found() {
        when(accesorioRepository.findById(1)).thenReturn(Optional.of(accesorio));

        Optional<Accesorio> result = accesorioService.getAccesorioById(1);

        assertTrue(result.isPresent());
        assertEquals("Volante Logitech G29", result.get().getNombre());
        verify(accesorioRepository, times(1)).findById(1);
    }

    @Test
    void testGetAccesorioById_NotFound() {
        when(accesorioRepository.findById(2)).thenReturn(Optional.empty());

        Optional<Accesorio> result = accesorioService.getAccesorioById(2);

        assertFalse(result.isPresent());
        verify(accesorioRepository, times(1)).findById(2);
    }

    @Test
    void testSaveAccesorio() {
        when(accesorioRepository.save(any(Accesorio.class))).thenReturn(accesorio);

        Accesorio result = accesorioService.saveAccesorio(accesorio);

        assertNotNull(result);
        assertEquals("Volante Logitech G29", result.getNombre());
        verify(accesorioRepository, times(1)).save(accesorio);
        verify(historialRepository, times(1)).save(any(HistorialAccesorio.class));
    }

    @Test
    void testDeleteAccesorio_Found() {
        when(accesorioRepository.findById(1)).thenReturn(Optional.of(accesorio));

        accesorioService.deleteAccesorio(1);

        verify(accesorioRepository, times(1)).deleteById(1);
        verify(historialRepository, times(1)).save(any(HistorialAccesorio.class));
    }

    @Test
    void testDeleteAccesorio_NotFound() {
        when(accesorioRepository.findById(2)).thenReturn(Optional.empty());

        accesorioService.deleteAccesorio(2);

        verify(accesorioRepository, never()).deleteById(anyInt());
        verify(historialRepository, never()).save(any(HistorialAccesorio.class));
    }

    @Test
    void testReduceStock() {
        when(accesorioRepository.findById(1)).thenReturn(Optional.of(accesorio));

        accesorioService.reduceStock(1);

        verify(historialRepository, times(1)).save(any(HistorialAccesorio.class));
    }
}
