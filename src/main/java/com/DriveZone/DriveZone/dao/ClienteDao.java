package com.DriveZone.DriveZone.dao;

import com.DriveZone.DriveZone.models.Cliente;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Repositorio para la entidad {@link Cliente}.
 * <p>
 * Extiende {@link JpaRepository}, lo que proporciona métodos CRUD (Crear, Leer, Actualizar y Eliminar)
 * de manera predeterminada sin necesidad de una implementación manual.
 * </p>
 *
 * <p>
 * Esta interfaz permite realizar operaciones sobre la base de datos para la entidad {@link Cliente}.
 * </p>
 *
 * @author DriveZone
 * @see Cliente
 * @see JpaRepository
 */
public interface ClienteDao extends JpaRepository<Cliente, Integer> {
}
