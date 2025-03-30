package com.DriveZone.DriveZone.repository;

import com.DriveZone.DriveZone.models.AccesorioHasOrdenCompra;
import com.DriveZone.DriveZone.models.AccesorioHasOrdenCompraId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Repositorio para gestionar las operaciones de base de datos relacionadas con la entidad {@link AccesorioHasOrdenCompra}.
 * <p>
 * Proporciona métodos para realizar consultas y persistencia de datos en la tabla intermedia
 * que relaciona accesorios con órdenes de compra.
 * </p>
 *
 * <h2>Ejemplo de uso:</h2>
 * <pre>
 *     {@code
 *     @Autowired
 *     private AccesorioOrdenCompraRepository repository;
 *
 *     List<AccesorioHasOrdenCompra> accesorios = repository.findById_IdOrdenCompra(1);
 *     }
 * </pre>
 *
 * @author DriveZone Team
 * @version 1.1
 * @since 2025-03-30
 */
@Repository
public interface AccesorioOrdenCompraRepository extends JpaRepository<AccesorioHasOrdenCompra, AccesorioHasOrdenCompraId> {
    /**
     * Busca todos los accesorios asociados a una orden de compra específica.
     *
     * @param idOrdenCompra ID de la orden de compra.
     * @return Lista de {@link AccesorioHasOrdenCompra} asociados a la orden de compra.
     */
    List<AccesorioHasOrdenCompra> findById_IdOrdenCompra(int idOrdenCompra);

}
