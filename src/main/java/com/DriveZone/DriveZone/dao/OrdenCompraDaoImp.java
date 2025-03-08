package com.DriveZone.DriveZone.dao;

import com.DriveZone.DriveZone.models.OrdenCompra;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

/**
 * Implementación del DAO para la gestión de órdenes de compra.
 * Proporciona métodos para obtener, guardar y eliminar órdenes de compra en la base de datos.
 */
@Repository
@Transactional
public class OrdenCompraDaoImp {
    @Autowired
    private OrdenCompraDao ordenCompraDao;

    /**
     * Obtiene todas las órdenes de compra almacenadas en la base de datos.
     *
     * @return Lista de todas las órdenes de compra.
     */
    public List<OrdenCompra> obtenerTodasLasOrdenes() {
        return ordenCompraDao.findAll();
    }

    /**
     * Busca una orden de compra por su ID.
     *
     * @param id Identificador único de la orden de compra.
     * @return Un {@link Optional} que contiene la orden de compra si se encuentra, o vacío si no existe.
     */
    public Optional<OrdenCompra> obtenerOrdenPorId(int id) {
        return ordenCompraDao.findById(id);
    }

    /**
     * Guarda una nueva orden de compra o actualiza una existente en la base de datos.
     *
     * @param orden Objeto de tipo {@link OrdenCompra} a guardar.
     * @return La orden de compra guardada o actualizada.
     */
    public OrdenCompra guardarOrden(OrdenCompra orden) {
        return ordenCompraDao.save(orden);
    }

    /**
     * Elimina una orden de compra de la base de datos según su ID.
     *
     * @param id Identificador único de la orden de compra a eliminar.
     */
    public void eliminarOrden(int id) {
        ordenCompraDao.deleteById(id);
    }


}
