package com.DriveZone.DriveZone.repository;

import com.DriveZone.DriveZone.models.OrdenCompra;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Repositorio para la entidad {@link OrdenCompra}.
 * <p>
 * Proporciona operaciones CRUD para la gestión de órdenes de compra en la base de datos.
 * </p>
 *
 * @author DriveZone Team
 * @version 1.1
 * @since 2025-03-30
 */
public interface OrdenCompraRepository extends JpaRepository<OrdenCompra, Integer> {
}
