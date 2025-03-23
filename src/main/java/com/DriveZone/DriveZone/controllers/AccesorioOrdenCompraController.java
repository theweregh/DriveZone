package com.DriveZone.DriveZone.controllers;



import com.DriveZone.DriveZone.models.AccesorioHasOrdenCompra;
import com.DriveZone.DriveZone.services.AccesorioOrdenCompraService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/accesorios-ordenes")
public class AccesorioOrdenCompraController {
    private final AccesorioOrdenCompraService accesorioOrdenCompraService;

    public AccesorioOrdenCompraController(AccesorioOrdenCompraService accesorioOrdenCompraService) {
        this.accesorioOrdenCompraService = accesorioOrdenCompraService;
    }

    @PostMapping
    public ResponseEntity<AccesorioHasOrdenCompra > agregarAccesorioAOrden(@RequestBody AccesorioHasOrdenCompra asociacion) {
        AccesorioHasOrdenCompra  guardado = accesorioOrdenCompraService.agregarAccesorioAOrden(asociacion);
        return ResponseEntity.ok(guardado);
    }

    @GetMapping("/{idOrden}")
    public ResponseEntity<List<AccesorioHasOrdenCompra >> obtenerAccesoriosDeOrden(@PathVariable int idOrden) {
        List<AccesorioHasOrdenCompra > accesorios = accesorioOrdenCompraService.obtenerAccesoriosDeOrden(idOrden);
        return ResponseEntity.ok(accesorios);
    }
}
