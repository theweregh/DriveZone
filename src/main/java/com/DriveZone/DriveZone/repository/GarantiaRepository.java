package com.DriveZone.DriveZone.repository;

import com.DriveZone.DriveZone.models.Garantia;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Repositorio para acceder a las operaciones CRUD de {@link Garantia}.
 * Utiliza Spring Data JPA para realizar operaciones sobre la base de datos.
 * <p>
 * Extiende de {@link JpaRepository}, proporcionando métodos como:
 * <ul>
 *     <li>findAll()</li>
 *     <li>findById(Integer id)</li>
 *     <li>save(Garantia entity)</li>
 *     <li>deleteById(Integer id)</li>
 * </ul>
 * entre otros métodos personalizados si se requieren en el futuro.
 *
 * @author DriveZone Team
 * @version 1.1
 * @since 2025-04-27
 */
public interface GarantiaRepository extends JpaRepository<Garantia, Integer> {
}
