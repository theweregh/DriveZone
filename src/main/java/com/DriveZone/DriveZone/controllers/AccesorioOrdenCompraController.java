package com.DriveZone.DriveZone.controllers;


import com.DriveZone.DriveZone.models.AccesorioHasOrdenCompra;
import com.DriveZone.DriveZone.services.AccesorioOrdenCompraService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Controlador REST para gestionar la relación entre accesorios y órdenes de compra.
 * Permite agregar accesorios a una orden de compra y obtener los accesorios de una orden específica.
 */
@RestController
@RequestMapping("/api/accesorios-ordenes")
public class AccesorioOrdenCompraController {
    private final AccesorioOrdenCompraService accesorioOrdenCompraService;

    /**
     * Constructor del controlador que inyecta el servicio de accesorios en órdenes de compra.
     *
     * @param accesorioOrdenCompraService Servicio para gestionar la relación entre accesorios y órdenes de compra.
     */
    public AccesorioOrdenCompraController(AccesorioOrdenCompraService accesorioOrdenCompraService) {
        this.accesorioOrdenCompraService = accesorioOrdenCompraService;
    }

    /**
     * Agrega un accesorio a una orden de compra.
     *
     * @param asociacion Objeto que representa la relación entre un accesorio y una orden de compra.
     * @return La asociación creada entre el accesorio y la orden de compra.
     */
    @PostMapping
    public ResponseEntity<AccesorioHasOrdenCompra> agregarAccesorioAOrden(@RequestBody AccesorioHasOrdenCompra asociacion) {
        AccesorioHasOrdenCompra guardado = accesorioOrdenCompraService.agregarAccesorioAOrden(asociacion);
        return ResponseEntity.ok(guardado);
    }

    /**
     * Obtiene la lista de accesorios asociados a una orden de compra específica.
     *
     * @param idOrden Identificador de la orden de compra.
     * @return Lista de accesorios asociados a la orden de compra.
     */
    @GetMapping("/{idOrden}")
    public ResponseEntity<List<AccesorioHasOrdenCompra>> obtenerAccesoriosDeOrden(@PathVariable int idOrden) {
        List<AccesorioHasOrdenCompra> accesorios = accesorioOrdenCompraService.obtenerAccesoriosDeOrden(idOrden);
        return ResponseEntity.ok(accesorios);
    }
}
