package com.DriveZone.DriveZone.dao;

import com.DriveZone.DriveZone.models.OrdenCompra;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Repositorio para la gestión de órdenes de compra.
 * Proporciona operaciones CRUD mediante la extensión de {@link JpaRepository}.
 */
@Repository
public interface OrdenCompraDao extends JpaRepository<OrdenCompra, Integer> {

}
