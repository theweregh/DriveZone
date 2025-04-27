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

/**
 * Servicio para la gestión de garantías.
 * <p>
 * Proporciona operaciones CRUD sobre las garantías y permite actualizar el estado de una garantía.
 * </p>
 *
 * @author DriveZone Team
 * @version 1.1
 * @since 2025-04-27
 */
@Service
public class GarantiaService {
    @Autowired
    private GarantiaRepository garantiaRepository;
    @Autowired
    private FacturaRepository facturaRepository;

    /**
     * Obtiene todas las garantías almacenadas en el sistema.
     *
     * @return Lista de garantías.
     */
    public List<Garantia> listarTodas() {
        return garantiaRepository.findAll();
    }

    /**
     * Busca una garantía por su identificador.
     *
     * @param id ID de la garantía.
     * @return Un objeto {@link Optional} con la garantía si existe, vacío en caso contrario.
     */
    public Optional<Garantia> buscarPorId(Integer id) {
        return garantiaRepository.findById(id);
    }

    /**
     * Guarda una nueva garantía en el sistema.
     * También vincula la garantía con una factura existente a través de su ID.
     *
     * @param garantia La garantía a guardar.
     * @return La garantía guardada.
     * @throws RuntimeException Si no se encuentra la factura asociada a la garantía.
     */
    public Garantia guardar(Garantia garantia) {
        // Obtener la factura desde la base de datos (por id)
        Factura facturaReal = facturaRepository.findById(garantia.getFactura().getIdFactura())
                .orElseThrow(() -> new RuntimeException("Factura no encontrada"));
        garantia.setFactura(facturaReal);

        return garantiaRepository.save(garantia);
    }

    /**
     * Elimina una garantía por su ID.
     *
     * @param id ID de la garantía a eliminar.
     */
    public void eliminar(Integer id) {
        garantiaRepository.deleteById(id);
    }

    /**
     * Actualiza el estado de una garantía.
     *
     * @param id          ID de la garantía a actualizar.
     * @param nuevoEstado El nuevo estado de la garantía.
     * @return La garantía con el estado actualizado.
     * @throws RuntimeException Si no se encuentra la garantía.
     */
    public Garantia actualizarEstado(Integer id, GarantiaEstado nuevoEstado) {
        Garantia garantia = garantiaRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Garantía no encontrada"));

        garantia.setEstado(nuevoEstado);
        return garantiaRepository.save(garantia);
    }
}
