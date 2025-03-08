package com.DriveZone.DriveZone.dao;

import com.DriveZone.DriveZone.models.OrdenCompra;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Implementación del acceso a datos para la gestión de órdenes de compra.
 */
@Repository
@Transactional
public class CarritoCompraDaoImp {
    @PersistenceContext
    EntityManager entityManager;

    /**
     * Obtiene todas las órdenes de compra almacenadas en la base de datos.
     *
     * @return Una lista de {@link OrdenCompra}.
     */
    public List<OrdenCompra> getOrdenesCompra() {
        String query = "FROM OrdenCompra";
        return entityManager.createQuery(query, OrdenCompra.class).getResultList();
    }

    /**
     * Obtiene una orden de compra específica por su ID.
     *
     * @param id El ID de la orden de compra.
     * @return La instancia de {@link OrdenCompra} si se encuentra, o {@code null} si no existe.
     */
    public OrdenCompra getOrdenCompraById(int id) {
        return entityManager.find(OrdenCompra.class, id);
    }

    /**
     * Elimina una orden de compra de la base de datos si existe.
     *
     * @param id El ID de la orden de compra a eliminar.
     */
    public void deleteOrdenCompra(int id) {
        OrdenCompra orden = entityManager.find(OrdenCompra.class, id);
        if (orden != null) {
            entityManager.remove(orden);
        }
    }

    /**
     * Registra o actualiza una orden de compra en la base de datos.
     *
     * @param ordenCompra La orden de compra a registrar o actualizar.
     */
    public void registrar(OrdenCompra ordenCompra) {
        entityManager.merge(ordenCompra);
    }

}
