package com.DriveZone.DriveZone.services;

import com.DriveZone.DriveZone.models.Factura;
import com.DriveZone.DriveZone.models.Garantia;
import com.DriveZone.DriveZone.models.GarantiaEstado;
import com.DriveZone.DriveZone.repository.FacturaRepository;
import com.DriveZone.DriveZone.repository.GarantiaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class GarantiaService {
    @Autowired
    private GarantiaRepository garantiaRepository;
    @Autowired
private FacturaRepository facturaRepository;
    public List<Garantia> listarTodas() {
        return garantiaRepository.findAll();
    }

    public Optional<Garantia> buscarPorId(Integer id) {
        return garantiaRepository.findById(id);
    }
/*
    public Garantia guardar(Garantia garantia) {
        return garantiaRepository.save(garantia);
    }*/

public Garantia guardar(Garantia garantia) {
    // Obtener la factura desde la base de datos (por id)
    Factura facturaReal = facturaRepository.findById(garantia.getFactura().getIdFactura())
                             .orElseThrow(() -> new RuntimeException("Factura no encontrada"));
    garantia.setFactura(facturaReal);

    return garantiaRepository.save(garantia);
}

    public void eliminar(Integer id) {
        garantiaRepository.deleteById(id);
    }
    public Garantia actualizarEstado(Integer id, GarantiaEstado nuevoEstado) {
        Garantia garantia = garantiaRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Garantía no encontrada"));

        garantia.setEstado(nuevoEstado);
        return garantiaRepository.save(garantia);
    }
}
