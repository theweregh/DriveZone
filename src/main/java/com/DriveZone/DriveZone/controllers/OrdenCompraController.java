package com.DriveZone.DriveZone.controllers;


import com.DriveZone.DriveZone.dao.OrdenCompraDaoImp;
import com.DriveZone.DriveZone.models.OrdenCompra;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/ordenes-compra")
public class OrdenCompraController {
    @Autowired
    private OrdenCompraDaoImp ordenCompraDao;

    @GetMapping
    public List<OrdenCompra> getOrdenesCompra() {
        return ordenCompraDao.obtenerTodasLasOrdenes();
    }

    @GetMapping("/{id}")
    public OrdenCompra getOrdenCompra(@PathVariable int id) {
        return ordenCompraDao.obtenerOrdenPorId(id).get();
    }

    @PostMapping
    public void registrarOrden(@RequestBody OrdenCompra ordenCompra) {
        ordenCompraDao.guardarOrden(ordenCompra);
    }

    @DeleteMapping("/{id}")
    public void deleteOrdenCompra(@PathVariable int id) {
        ordenCompraDao.eliminarOrden(id);
    }
}
