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

@Service
public class AccesorioService {
    @Autowired
    private AccesorioRepository accesorioRepository;
    @Autowired
    private HistorialAccesorioRepository historialRepository;

    public List<Accesorio> getAllAccesorios() {
        return accesorioRepository.findAll();
    }

    public Optional<Accesorio> getAccesorioById(int id) {
        return accesorioRepository.findById(id);
    }

    public Accesorio saveAccesorio(Accesorio accesorio) {
        Accesorio nuevoAccesorio = accesorioRepository.save(accesorio);

        // Guardar en historial
        historialRepository.save(new HistorialAccesorio(Accion.AGREGADO,
                                                        nuevoAccesorio.getNombre(),
                                                        nuevoAccesorio.getId()));

        return nuevoAccesorio;
    }

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
}
