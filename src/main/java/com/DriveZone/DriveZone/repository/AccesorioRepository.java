package com.DriveZone.DriveZone.repository;

import com.DriveZone.DriveZone.models.Accesorio;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Repositorio para gestionar las operaciones de base de datos relacionadas con la entidad {@link Accesorio}.
 * <p>
 * Proporciona métodos para realizar operaciones CRUD sobre la tabla de accesorios.
 * </p>
 *
 * <h2>Ejemplo de uso:</h2>
 * <pre>
 *     {@code
 *     @Autowired
 *     private AccesorioRepository accesorioRepository;
 *
 *     // Guardar un accesorio
 *     Accesorio accesorio = new Accesorio();
 *     accesorio.setNombre("Volante de carreras");
 *     accesorioRepository.save(accesorio);
 *
 *     // Obtener todos los accesorios
 *     List<Accesorio> accesorios = accesorioRepository.findAll();
 *     }
 * </pre>
 *
 * @author DriveZone Team
 * @version 1.1
 * @since 2025-03-30
 */
public interface AccesorioRepository extends JpaRepository<Accesorio, Integer> {
}
