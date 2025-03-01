package com.DriveZone.DriveZone.controllers;

import com.DriveZone.DriveZone.models.Accesorio;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/carrito")
public class CarritoController {
    private final List<Accesorio> carrito = new ArrayList<>(); // Lista en memoria

    @PostMapping("/agregar")
    public ResponseEntity<String> agregarAccesorio(@RequestBody Accesorio accesorio) {
        carrito.add(accesorio);
        return ResponseEntity.ok("Accesorio agregado al carrito");
    }

    @GetMapping
    public List<Accesorio> obtenerCarrito() {
        return carrito; // Devuelve el carrito actualizado
    }
    // 🔹 Método para actualizar la cantidad de un accesorio en el carrito
    @PutMapping("/actualizar/{id}")
    public ResponseEntity<String> actualizarCantidad(@PathVariable Long id, @RequestBody Accesorio nuevoAccesorio) {
        Optional<Accesorio> accesorioExistente = carrito.stream()
                .filter(accesorio -> accesorio.getId()== (id))
                .findFirst();

        if (accesorioExistente.isPresent()) {
            accesorioExistente.get().setStock(nuevoAccesorio.getStock());
            return ResponseEntity.ok("Cantidad actualizada correctamente");
        } else {
            return ResponseEntity.badRequest().body("Accesorio no encontrado en el carrito");
        }
    }
    @DeleteMapping("/eliminar/{id}")
public ResponseEntity<String> eliminarAccesorio(@PathVariable int id) {
    boolean eliminado = carrito.removeIf(accesorio -> accesorio.getId() == id);

    if (eliminado) {
        return ResponseEntity.ok("Accesorio eliminado correctamente");
    } else {
        return ResponseEntity.status(404).body("Accesorio no encontrado en el carrito");
    }
}
}
