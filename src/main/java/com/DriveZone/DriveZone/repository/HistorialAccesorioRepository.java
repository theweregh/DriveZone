package com.DriveZone.DriveZone.repository;

import com.DriveZone.DriveZone.models.HistorialAccesorio;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Repositorio para la entidad {@link HistorialAccesorio}.
 * <p>
 * Proporciona operaciones CRUD para la gestión del historial de accesorios en la base de datos.
 * </p>
 *
 * @author DriveZone Team
 * @version 1.1
 * @since 2025-03-30
 */
public interface HistorialAccesorioRepository extends JpaRepository<HistorialAccesorio, Integer> {
}
