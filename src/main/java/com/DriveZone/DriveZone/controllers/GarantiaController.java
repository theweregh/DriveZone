package com.DriveZone.DriveZone.controllers;

import com.DriveZone.DriveZone.models.Garantia;
import com.DriveZone.DriveZone.services.GarantiaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/garantias")
@CrossOrigin(origins = "*")
public class GarantiaController {
    @Autowired
    private GarantiaService garantiaService;

    @GetMapping
    public List<Garantia> listar() {
        return garantiaService.listarTodas();
    }

    @GetMapping("/{id}")
    public Optional<Garantia> buscar(@PathVariable Integer id) {
        return garantiaService.buscarPorId(id);
    }

    @PostMapping
    public Garantia crear(@RequestBody Garantia garantia) {
        return garantiaService.guardar(garantia);
    }

    @PutMapping("/{id}")
    public Garantia actualizar(@PathVariable Integer id, @RequestBody Garantia nueva) {
        return garantiaService.buscarPorId(id)
                .map(g -> {
                    g.setEstado(nueva.getEstado());
                    return garantiaService.guardar(g);
                }).orElseThrow();
    }

    @DeleteMapping("/{id}")
    public void eliminar(@PathVariable Integer id) {
        garantiaService.eliminar(id);
    }
}
