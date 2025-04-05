package com.DriveZone.DriveZone.repository;

import com.DriveZone.DriveZone.models.Factura;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
 * Repositorio para la entidad {@link Factura}.
 * <p>
 * Proporciona operaciones CRUD para la gestión de facturas en la base de datos.
 * </p>
 *
 * @author DriveZone Team
 * @version 1.1
 * @since 2025-03-30
 */
public interface FacturaRepository extends JpaRepository<Factura, Integer> {
    //List<Factura> findByOrdenCompra_Usuario_Email(String email);
    //List<Factura> findByUsuario_Email(String email);
    List<Factura> findByOrdenCompraUsuarioUsername(String username);
}
