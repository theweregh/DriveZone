package com.DriveZone.DriveZone.controllers;

import com.DriveZone.DriveZone.models.Accesorio;
import com.DriveZone.DriveZone.models.CarritoCompra;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/carrito")
public class CarritoController {
    private final List<CarritoCompra> carrito = new ArrayList<>();

    // 🔹 Obtener el carrito
    @GetMapping
    public ResponseEntity<List<CarritoCompra>> obtenerCarrito() {
        return ResponseEntity.ok(carrito);
    }

    // 🔹 Agregar producto al carrito
    @PostMapping("/agregar")
    public ResponseEntity<String> agregarAlCarrito(@RequestBody CarritoCompra nuevoProducto) {
        for (CarritoCompra item : carrito) {
            if (item.getAccesorio().getId() == nuevoProducto.getAccesorio().getId()) {
                item.setCantidad(item.getCantidad() + nuevoProducto.getCantidad());
                return ResponseEntity.ok("Cantidad actualizada en el carrito");
            }
        }
        carrito.add(nuevoProducto);
        return ResponseEntity.ok("Producto agregado al carrito");
    }

    // 🔹 Actualizar cantidad de un producto en el carrito
    @PutMapping("/actualizar/{id}")
    public ResponseEntity<String> actualizarCantidad(@PathVariable Long id, @RequestBody CarritoCompra productoActualizado) {
        for (CarritoCompra item : carrito) {
            if (item.getAccesorio().getId() == id) {
                item.setCantidad(productoActualizado.getCantidad());
                return ResponseEntity.ok("Cantidad actualizada");
            }
        }
        return ResponseEntity.badRequest().body("Producto no encontrado en el carrito");
    }

    // 🔹 Eliminar producto del carrito
    @DeleteMapping("/eliminar/{id}")
    public ResponseEntity<String> eliminarDelCarrito(@PathVariable Long id) {
        carrito.removeIf(item -> item.getAccesorio().getId() == id);
        return ResponseEntity.ok("Producto eliminado del carrito");
    }

    // 🔹 Procesar compra (vaciar carrito)
    @PostMapping("/procesar")
    public ResponseEntity<String> procesarCompra() {
        carrito.clear();
        return ResponseEntity.ok("Compra procesada con éxito");
    }
}
