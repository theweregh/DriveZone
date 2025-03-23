package com.DriveZone.DriveZone.controllers;

import com.DriveZone.DriveZone.models.OrdenCompra;
import com.DriveZone.DriveZone.services.OrdenCompraService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/ordenes")
public class OrdenCompraController {
    @Autowired
    private OrdenCompraService ordenCompraService;

    // Obtener todas las órdenes de compra
    @GetMapping
    public ResponseEntity<List<OrdenCompra>> obtenerOrdenes() {
        List<OrdenCompra> ordenes = ordenCompraService.obtenerTodasLasOrdenes();
        return ResponseEntity.ok(ordenes);
    }

    // Obtener una orden por ID
    @GetMapping("/{id}")
    public ResponseEntity<OrdenCompra> obtenerOrdenPorId(@PathVariable Integer id) {
        return ordenCompraService.obtenerOrdenPorId(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    // Crear una nueva orden de compra
    @PostMapping
    public ResponseEntity<OrdenCompra> crearOrden(@RequestBody OrdenCompra orden) {
        OrdenCompra nuevaOrden = ordenCompraService.crearOrden(orden);
        return ResponseEntity.ok(nuevaOrden);
    }

    // Eliminar una orden de compra por ID
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminarOrden(@PathVariable Integer id) {
        ordenCompraService.eliminarOrden(id);
        return ResponseEntity.noContent().build();
    }
}
