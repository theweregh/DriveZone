package com.DriveZone.DriveZone.controllers;


import com.DriveZone.DriveZone.dao.OrdenCompraDaoImp;
import com.DriveZone.DriveZone.models.OrdenCompra;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Controlador REST para gestionar órdenes de compra en el sistema.
 */
@RestController
@RequestMapping("/ordenes-compra")
public class OrdenCompraController {
    @Autowired
    private OrdenCompraDaoImp ordenCompraDao;

    /**
     * Obtiene la lista de todas las órdenes de compra registradas.
     *
     * @return Lista de objetos {@link OrdenCompra}.
     */
    @GetMapping
    public List<OrdenCompra> getOrdenesCompra() {
        return ordenCompraDao.obtenerTodasLasOrdenes();
    }

    /**
     * Obtiene una orden de compra específica por su ID.
     *
     * @param id ID de la orden de compra a buscar.
     * @return Objeto {@link OrdenCompra} si se encuentra.
     */
    @GetMapping("/{id}")
    public OrdenCompra getOrdenCompra(@PathVariable int id) {
        return ordenCompraDao.obtenerOrdenPorId(id).get();
    }

    /**
     * Registra una nueva orden de compra en la base de datos.
     *
     * @param ordenCompra Objeto {@link OrdenCompra} a registrar.
     */
    @PostMapping
    public void registrarOrden(@RequestBody OrdenCompra ordenCompra) {
        ordenCompraDao.guardarOrden(ordenCompra);
    }

    /**
     * Elimina una orden de compra de la base de datos.
     *
     * @param id ID de la orden de compra a eliminar.
     */
    @DeleteMapping("/{id}")
    public void deleteOrdenCompra(@PathVariable int id) {
        ordenCompraDao.eliminarOrden(id);
    }
}
