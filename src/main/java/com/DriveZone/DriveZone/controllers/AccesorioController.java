package com.DriveZone.DriveZone.controllers;

import com.DriveZone.DriveZone.dao.AccesorioDao;
import com.DriveZone.DriveZone.models.Accesorio;
import com.DriveZone.DriveZone.services.AccesorioService;
import com.DriveZone.DriveZone.utils.JWTUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

/**
 * Controlador REST para gestionar los accesorios en la aplicación.
 * Proporciona endpoints para obtener accesorios individuales y listar todos los accesorios disponibles.
 */
@RestController
public class AccesorioController {
    @Autowired
    private AccesorioDao accesorioDao;
    @Autowired
    private JWTUtil jwtUtil;
    @Autowired
    private AccesorioService accesorioService;
    /**
     * Obtiene un accesorio específico según su ID.
     *
     * @param id Identificador del accesorio a recuperar.
     * @return El accesorio correspondiente al ID proporcionado.
     */
    @RequestMapping(value = "api/accesorio/{id}", method = RequestMethod.GET)
    public Accesorio getAccesorio(@PathVariable int id, @RequestHeader(value = "Authorization") String token) {
        System.out.println("Token recibido: " + token);
    String userId = jwtUtil.getKey(token);
    System.out.println("Usuario ID extraído: " + userId);
    if (!validarToken(token)) {
        return null;
    }

    return accesorioDao.findById(id).orElse(null);
    }

    /**
     * Obtiene la lista de todos los accesorios disponibles.
     *
     * @param token Token de autorización enviado en el encabezado de la solicitud.
     * @return Lista de accesorios si la autenticación es válida, de lo contrario retorna {@code null}.
     */
    @RequestMapping(value = "api/accesorio", method = RequestMethod.GET)
    public List<Accesorio> getAccesorios(@RequestHeader(value = "Authorization") String token) {
        System.out.println("Token recibido: " + token);
        String userId = jwtUtil.getKey(token);
        System.out.println("Usuario ID extraído: " + userId);
        if (!validarToken(token)) {
            return null;
        }

        return accesorioDao.findAll();
    }

    /**
     * Valida el token de autenticación verificando si es válido y contiene un ID de usuario.
     *
     * @param token Token JWT a validar.
     * @return {@code true} si el token es válido, {@code false} en caso contrario.
     */
    private boolean validarToken(String token) {
        String userId = jwtUtil.getKey(token);
        //se puede verificar si este user esta en la db
        return userId != null;
    }/*
    @PutMapping("/api/accesorios/reducir-stock")
    public ResponseEntity<?> reducirStock(@RequestBody List<Accesorio> accesorios, @RequestHeader(value = "Authorization") String token) {
        if (!validarToken(token)) {
            return ResponseEntity.status(403).body("Acceso no autorizado");
        }

        for (Accesorio item : accesorios) {
            Accesorio accesorio = accesorioDao.findById(item.getId()).orElse(null);
            if (accesorio != null) {
                int nuevoStock = accesorio.getStock() - item.getStock();
                if (nuevoStock < 0) {
                    return ResponseEntity.badRequest().body("Stock insuficiente para " + accesorio.getNombre());
                }
                accesorio.setStock(nuevoStock);
                accesorioDao.save(accesorio);
            }
        }
        return ResponseEntity.ok("Stock actualizado correctamente");
    }*/
    @PutMapping("/api/accesorios/reducir-stock")
public ResponseEntity<?> reducirStock(@RequestBody List<Accesorio> accesorios, @RequestHeader(value = "Authorization") String token) {
    if (!validarToken(token)) {
        return ResponseEntity.status(403).body("Acceso no autorizado");
    }

    boolean stockModificado = false; // Bandera para saber si al menos un accesorio se modificó

    for (Accesorio item : accesorios) {
        Accesorio accesorio = accesorioDao.findById(item.getId()).orElse(null);
        System.out.println("Accesorio encontrado: " + accesorio);
        System.out.println("Item recibido: " + item);
        if (accesorio != null) {
            int nuevoStock = accesorio.getStock() - item.getStock();
            System.out.println("Stock actual para " + accesorio.getNombre() + ": " + accesorio.getStock());
            System.out.println("Stock a reducir para " + accesorio.getNombre() + ": " + item.getStock());
            System.out.println("Nuevo stock para " + accesorio.getNombre() + ": " + nuevoStock);
            if (nuevoStock < 0) {
                return ResponseEntity.badRequest().body("Stock insuficiente para " + accesorio.getNombre());
            }
            accesorio.setStock(nuevoStock);
            accesorioDao.save(accesorio);
            stockModificado = true; // Se modificó al menos un accesorio
        }
    }

    if (!stockModificado) {
        return ResponseEntity.badRequest().body("No se encontró ningún accesorio válido para reducir stock.");
    }

    return ResponseEntity.ok("Stock actualizado correctamente");
}
    @PostMapping("/accesorios")
    public Accesorio createAccesorio(@RequestBody Accesorio accesorio) {
        return accesorioService.saveAccesorio(accesorio);
    }

    @PutMapping("/accesorios/{id}")
    public ResponseEntity<Accesorio> updateAccesorio(@PathVariable int id, @RequestBody Accesorio accesorioDetails) {
        Optional<Accesorio> accesorioOptional = accesorioService.getAccesorioById(id);
        if (accesorioOptional.isPresent()) {
            Accesorio accesorio = accesorioOptional.get();
            accesorio.setNombre(accesorioDetails.getNombre());
            accesorio.setDescripcion(accesorioDetails.getDescripcion());
            accesorio.setStock(accesorioDetails.getStock());
            accesorio.setPrecioVenta(accesorioDetails.getPrecioVenta());
            accesorio.setImagen(accesorioDetails.getImagen());
            accesorio.setDescuento(accesorioDetails.getDescuento());
            return ResponseEntity.ok(accesorioService.saveAccesorio(accesorio));
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/accesorios/{id}")
    public ResponseEntity<Void> deleteAccesorio(@PathVariable int id) {
        if (accesorioService.getAccesorioById(id).isPresent()) {
            accesorioService.deleteAccesorio(id);
            return ResponseEntity.noContent().build();
        } else {
            return ResponseEntity.notFound().build();
        }
    }
}
