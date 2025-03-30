package com.DriveZone.DriveZone.services;

import com.DriveZone.DriveZone.models.AccesorioHasOrdenCompra;
import com.DriveZone.DriveZone.models.AccesorioHasOrdenCompraId;
import com.DriveZone.DriveZone.repository.AccesorioOrdenCompraRepository;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Servicio para gestionar la relación entre accesorios y órdenes de compra.
 * <p>
 * Permite agregar accesorios a una orden de compra y obtener los accesorios asociados a una orden específica.
 * </p>
 *
 * @author DriveZone Team
 * @version 1.1
 * @since 2025-03-30
 */
@Service
public class AccesorioOrdenCompraService {
    private final AccesorioOrdenCompraRepository accesorioOrdenCompraRepository;

    /**
     * Constructor del servicio.
     *
     * @param accesorioOrdenCompraRepository Repositorio de accesorios en órdenes de compra.
     */
    public AccesorioOrdenCompraService(AccesorioOrdenCompraRepository accesorioOrdenCompraRepository) {
        this.accesorioOrdenCompraRepository = accesorioOrdenCompraRepository;
    }

    /**
     * Agrega un accesorio a una orden de compra.
     *
     * @param asociacion Asociación entre el accesorio y la orden de compra.
     * @return La asociación guardada.
     * @throws IllegalArgumentException Si la orden de compra o el accesorio son nulos.
     */
    public AccesorioHasOrdenCompra agregarAccesorioAOrden(AccesorioHasOrdenCompra asociacion) {
        if (asociacion.getOrdenCompra() == null || asociacion.getAccesorio() == null) {
            throw new IllegalArgumentException("OrdenCompra o Accesorio no pueden ser nulos");
        }

        // Crear la clave compuesta
        AccesorioHasOrdenCompraId idCompuesto = new AccesorioHasOrdenCompraId();
        idCompuesto.setIdAccesorio(asociacion.getAccesorio().getId());
        idCompuesto.setIdOrdenCompra(asociacion.getOrdenCompra().getIdOrdenCompra());

        // Asignar el ID a la entidad antes de guardar
        asociacion.setId(idCompuesto);
        return accesorioOrdenCompraRepository.save(asociacion);
    }

    /**
     * Obtiene la lista de accesorios asociados a una orden de compra específica.
     *
     * @param idOrden ID de la orden de compra.
     * @return Lista de asociaciones de accesorios con la orden de compra.
     */
    public List<AccesorioHasOrdenCompra> obtenerAccesoriosDeOrden(int idOrden) {
        return accesorioOrdenCompraRepository.findById_IdOrdenCompra(idOrden);
    }
}
