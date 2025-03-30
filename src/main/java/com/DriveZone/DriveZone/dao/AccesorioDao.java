package com.DriveZone.DriveZone.dao;

import com.DriveZone.DriveZone.models.Accesorio;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Repositorio para la entidad {@link Accesorio}.
 * <p>
 * Extiende {@link JpaRepository}, lo que proporciona métodos CRUD (Crear, Leer, Actualizar y Eliminar)
 * de manera predeterminada sin necesidad de una implementación manual.
 * </p>
 *
 * <p>
 * Esta interfaz permite realizar operaciones sobre la base de datos para la entidad {@link Accesorio}.
 * </p>
 *
 * @author DriveZone
 * @see Accesorio
 * @see JpaRepository
 */
@Repository
public interface AccesorioDao extends JpaRepository<Accesorio, Integer> {

}
