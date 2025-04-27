package com.DriveZone.DriveZone.services;

import com.DriveZone.DriveZone.models.Accesorio;
import com.DriveZone.DriveZone.models.Accion;
import com.DriveZone.DriveZone.models.HistorialAccesorio;
import com.DriveZone.DriveZone.repository.AccesorioRepository;
import com.DriveZone.DriveZone.repository.HistorialAccesorioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

/**
 * Servicio para la gestión de accesorios.
 * <p>
 * Proporciona operaciones CRUD sobre los accesorios y mantiene un historial de acciones realizadas sobre ellos.
 * </p>
 *
 * @author DriveZone Team
 * @version 1.1
 * @since 2025-03-30
 */
@Service
public class AccesorioService {
    @Autowired
    private AccesorioRepository accesorioRepository;
    @Autowired
    private HistorialAccesorioRepository historialRepository;

    /**
     * Obtiene todos los accesorios disponibles en el sistema.
     *
     * @return Lista de accesorios.
     */
    public List<Accesorio> getAllAccesorios() {
        return accesorioRepository.findAll();
    }

    /**
     * Obtiene un accesorio por su identificador.
     *
     * @param id ID del accesorio.
     * @return Un objeto {@link Optional} con el accesorio si existe, vacío en caso contrario.
     */
    public Optional<Accesorio> getAccesorioById(int id) {
        return accesorioRepository.findById(id);
    }

    /**
     * Guarda un nuevo accesorio en el sistema y registra la acción en el historial.
     *
     * @param accesorio Accesorio a guardar.
     * @return El accesorio guardado.
     */
    public Accesorio saveAccesorio(Accesorio accesorio) {
        Accesorio nuevoAccesorio = accesorioRepository.save(accesorio);

        // Guardar en historial
        historialRepository.save(new HistorialAccesorio(Accion.AGREGADO,
                nuevoAccesorio.getNombre(),
                nuevoAccesorio.getId()));

        return nuevoAccesorio;
    }

    /**
     * Elimina un accesorio por su ID y registra la acción en el historial antes de eliminarlo.
     *
     * @param id ID del accesorio a eliminar.
     */
    public void deleteAccesorio(int id) {
        Optional<Accesorio> accesorioOptional = accesorioRepository.findById(id);
        if (accesorioOptional.isPresent()) {
            Accesorio accesorio = accesorioOptional.get();

            // Guardar en historial antes de eliminar
            historialRepository.save(new HistorialAccesorio(Accion.ELIMINADO,
                    accesorio.getNombre(),
                    accesorio.getId()));

            accesorioRepository.deleteById(id);
        }
    }

    /**
     * Reduce el stock de un accesorio y registra la acción en el historial.
     *
     * @param id ID del accesorio cuyo stock se va a reducir.
     */
    public void reduceStock(int id) {
        Optional<Accesorio> accesorioOptional = accesorioRepository.findById(id);
        if (accesorioOptional.isPresent()) {
            Accesorio accesorio = accesorioOptional.get();

            // Guardar en historial antes de eliminar
            historialRepository.save(new HistorialAccesorio(Accion.ELIMINADO,
                    accesorio.getNombre(),
                    accesorio.getId()));
        }
    }
    public Accesorio reduceStock(Accesorio accesorio) {
        Accesorio nuevoAccesorio = accesorioRepository.save(accesorio);

        // Guardar en historial
        historialRepository.save(new HistorialAccesorio(Accion.ELIMINADO,
                nuevoAccesorio.getNombre(),
                nuevoAccesorio.getId()));

        return nuevoAccesorio;
    }
}
