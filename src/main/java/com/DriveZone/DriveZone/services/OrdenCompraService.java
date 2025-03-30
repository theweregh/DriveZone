package com.DriveZone.DriveZone.services;

import com.DriveZone.DriveZone.models.OrdenCompra;
import com.DriveZone.DriveZone.repository.OrdenCompraRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

/**
 * Servicio para gestionar las órdenes de compra.
 *
 * @author DriveZone Team
 * @version 1.1
 * @since 2025-03-30
 */
@Service
public class OrdenCompraService {
    @Autowired
    private OrdenCompraRepository ordenCompraRepository;

    /**
     * Obtiene todas las órdenes de compra almacenadas en la base de datos.
     *
     * @return Lista de todas las órdenes de compra.
     */
    public List<OrdenCompra> obtenerTodasLasOrdenes() {
        return ordenCompraRepository.findAll();
    }

    /**
     * Obtiene una orden de compra específica por su ID.
     *
     * @param id Identificador único de la orden de compra.
     * @return Un {@link Optional} que puede contener la orden de compra si se encuentra.
     */
    public Optional<OrdenCompra> obtenerOrdenPorId(Integer id) {
        return ordenCompraRepository.findById(id);
    }

    /**
     * Crea una nueva orden de compra y la guarda en la base de datos.
     *
     * @param orden Objeto {@link OrdenCompra} con los datos de la nueva orden.
     * @return La orden de compra creada y guardada en la base de datos.
     */
    @Transactional
    public OrdenCompra crearOrden(OrdenCompra orden) {
        return ordenCompraRepository.save(orden);
    }

    /**
     * Elimina una orden de compra por su ID.
     *
     * @param id Identificador único de la orden de compra a eliminar.
     */
    @Transactional
    public void eliminarOrden(Integer id) {
        ordenCompraRepository.deleteById(id);
    }
}
