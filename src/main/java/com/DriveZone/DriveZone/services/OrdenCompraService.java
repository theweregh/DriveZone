package com.DriveZone.DriveZone.services;

import com.DriveZone.DriveZone.dao.AccesorioDao;
import com.DriveZone.DriveZone.dao.OrdenCompraDao;
import com.DriveZone.DriveZone.models.Accesorio;
import com.DriveZone.DriveZone.models.OrdenCompra;
import com.DriveZone.DriveZone.models.Usuario;
import com.DriveZone.DriveZone.repository.OrdenCompraRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
public class OrdenCompraService {
    @Autowired
    private OrdenCompraRepository ordenCompraRepository;

    // Obtener todas las órdenes de compra
    public List<OrdenCompra> obtenerTodasLasOrdenes() {
        return ordenCompraRepository.findAll();
    }

    // Obtener una orden de compra por ID
    public Optional<OrdenCompra> obtenerOrdenPorId(Integer id) {
        return ordenCompraRepository.findById(id);
    }

    // Crear una nueva orden de compra
    @Transactional
    public OrdenCompra crearOrden(OrdenCompra orden) {
        return ordenCompraRepository.save(orden);
    }

    // Eliminar una orden de compra por ID
    @Transactional
    public void eliminarOrden(Integer id) {
        ordenCompraRepository.deleteById(id);
    }
}
