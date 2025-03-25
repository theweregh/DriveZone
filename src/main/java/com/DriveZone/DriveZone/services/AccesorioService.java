package com.DriveZone.DriveZone.services;

import com.DriveZone.DriveZone.models.Accesorio;
import com.DriveZone.DriveZone.repository.AccesorioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class AccesorioService {
    @Autowired
    private AccesorioRepository accesorioRepository;

    public List<Accesorio> getAllAccesorios() {
        return accesorioRepository.findAll();
    }

    public Optional<Accesorio> getAccesorioById(int id) {
        return accesorioRepository.findById(id);
    }

    public Accesorio saveAccesorio(Accesorio accesorio) {
        return accesorioRepository.save(accesorio);
    }

    public void deleteAccesorio(int id) {
        accesorioRepository.deleteById(id);
    }
}
