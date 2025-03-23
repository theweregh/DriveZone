package com.DriveZone.DriveZone.services;

import com.DriveZone.DriveZone.controllers.AccesorioController;
import com.DriveZone.DriveZone.models.AccesorioHasOrdenCompra;
import com.DriveZone.DriveZone.models.AccesorioHasOrdenCompraId;
import com.DriveZone.DriveZone.repository.AccesorioOrdenCompraRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AccesorioOrdenCompraService {
    /*private final AccesorioOrdenCompraRepository repository;

    public AccesorioOrdenCompraService(AccesorioOrdenCompraRepository repository) {
        this.repository = repository;
    }

    public AccesorioHasOrdenCompra  agregarAccesorioAOrden(AccesorioHasOrdenCompra asociacion) {
        AccesorioHasOrdenCompra  accesorioHasOrdenCompra = asociacion;
        if (accesorioHasOrdenCompra.getAccesorio() == null) {
    throw new IllegalArgumentException("El accesorio no puede ser null");
}
        return repository.save(asociacion);
    }

    public List<AccesorioHasOrdenCompra > obtenerAccesoriosDeOrden(int idOrden) {
        return repository.findById_IdOrdenCompra(idOrden);
    }*/
    private final AccesorioOrdenCompraRepository accesorioOrdenCompraRepository;

    public AccesorioOrdenCompraService(AccesorioOrdenCompraRepository accesorioOrdenCompraRepository) {
        this.accesorioOrdenCompraRepository = accesorioOrdenCompraRepository;
    }

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

    public List<AccesorioHasOrdenCompra> obtenerAccesoriosDeOrden(int idOrden) {
        return accesorioOrdenCompraRepository.findById_IdOrdenCompra(idOrden);
    }
}
