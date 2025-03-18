package com.DriveZone.DriveZone.controllers;


import com.DriveZone.DriveZone.dao.OrdenCompraDaoImp;
import com.DriveZone.DriveZone.models.OrdenCompra;
import com.DriveZone.DriveZone.services.OrdenCompraService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

/**
 * Controlador REST para gestionar órdenes de compra en el sistema.
 */
@RestController
@RequestMapping("/ordenes-compra")
public class OrdenCompraController {
    @Autowired
    private OrdenCompraService ordenCompraService;

    /**
     * Obtiene todas las órdenes de compra.
     * @return Lista de órdenes de compra.
     */
    @GetMapping
    public List<OrdenCompra> obtenerTodasLasOrdenes() {
        return ordenCompraService.obtenerTodasLasOrdenes();
    }

    /**
     * Obtiene una orden de compra por su ID.
     * @param id ID de la orden de compra.
     * @return Orden de compra si existe, o un error 404 si no se encuentra.
     */
    @GetMapping("/{id}")
    public ResponseEntity<OrdenCompra> obtenerOrdenPorId(@PathVariable int id) {
        Optional<OrdenCompra> orden = ordenCompraService.obtenerOrdenPorId(id);
        return orden.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    /**
     * Crea una nueva orden de compra.
     * @param ordenCompra Datos de la nueva orden.
     * @return La orden de compra creada.
     */
    @PostMapping
    public ResponseEntity<OrdenCompra> crearOrdenCompra(@RequestBody OrdenCompra ordenCompra) {
        OrdenCompra nuevaOrden = ordenCompraService.crearOrdenCompra(ordenCompra);
        return ResponseEntity.ok(nuevaOrden);
    }

    /**
     * Elimina una orden de compra por su ID.
     * @param id ID de la orden a eliminar.
     * @return Respuesta 200 si se eliminó correctamente, o 404 si no se encontró.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminarOrden(@PathVariable int id) {
        if (ordenCompraService.eliminarOrden(id)) {
            return ResponseEntity.ok().build();
        }
        return ResponseEntity.notFound().build();
    }
}
