package com.DriveZone.DriveZone.controllers;

import com.DriveZone.DriveZone.models.OrdenCompra;
import com.DriveZone.DriveZone.services.OrdenCompraService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Controlador REST para la gestión de órdenes de compra.
 * Proporciona endpoints para obtener, crear y eliminar órdenes de compra en el sistema.
 */
@RestController
@RequestMapping("/api/ordenes")
public class OrdenCompraController {
    @Autowired
    private OrdenCompraService ordenCompraService;

    /**
     * Obtiene todas las órdenes de compra disponibles.
     *
     * @return {@link ResponseEntity} con la lista de todas las órdenes de compra registradas.
     */
    @GetMapping
    public ResponseEntity<List<OrdenCompra>> obtenerOrdenes() {
        List<OrdenCompra> ordenes = ordenCompraService.obtenerTodasLasOrdenes();
        return ResponseEntity.ok(ordenes);
    }

    /**
     * Obtiene una orden de compra por su ID.
     *
     * @param id Identificador de la orden de compra.
     * @return {@link ResponseEntity} con la orden de compra si se encuentra, o un estado 404 si no existe.
     */
    @GetMapping("/{id}")
    public ResponseEntity<OrdenCompra> obtenerOrdenPorId(@PathVariable Integer id) {
        return ordenCompraService.obtenerOrdenPorId(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    /**
     * Crea una nueva orden de compra.
     *
     * @param orden Objeto {@link OrdenCompra} con la información de la nueva orden.
     * @return {@link ResponseEntity} con la orden de compra creada.
     */
    @PostMapping
    public ResponseEntity<OrdenCompra> crearOrden(@RequestBody OrdenCompra orden) {
        OrdenCompra nuevaOrden = ordenCompraService.crearOrden(orden);
        return ResponseEntity.ok(nuevaOrden);
    }

    /**
     * Elimina una orden de compra por su ID.
     *
     * @param id Identificador de la orden de compra a eliminar.
     * @return {@link ResponseEntity} con un estado 204 si la eliminación fue exitosa.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminarOrden(@PathVariable Integer id) {
        ordenCompraService.eliminarOrden(id);
        return ResponseEntity.noContent().build();
    }
}
