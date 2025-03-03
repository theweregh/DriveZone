package com.DriveZone.DriveZone.controllers;

import com.DriveZone.DriveZone.dao.OrdenCompraDao;
import com.DriveZone.DriveZone.models.OrdenCompra;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/ordenes")
public class OrdenCompraController {
    @Autowired
    private OrdenCompraDao ordenCompraDao;

    // 🔹 Obtener todas las órdenes
    @GetMapping
    public List<OrdenCompra> obtenerOrdenes() {
        return ordenCompraDao.findAll();
    }

    // 🔹 Obtener orden por ID
    @GetMapping("/OrdenCompra/{id}")
    public Optional<OrdenCompra> obtenerOrden(@PathVariable int id) {
        return ordenCompraDao.findById(id);
    }

    // 🔹 Crear una nueva orden
    @PostMapping
    public OrdenCompra crearOrden(@RequestBody OrdenCompra orden) {
        return ordenCompraDao.save(orden);
    }

    // 🔹 Eliminar orden por ID
    @DeleteMapping("/OrdenCompra/{id}")
    public void eliminarOrden(@PathVariable int id) {
        ordenCompraDao.deleteById(id);
    }
}
