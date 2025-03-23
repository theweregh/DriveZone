package com.DriveZone.DriveZone.controllers;

import com.DriveZone.DriveZone.dao.ClienteDao;
import com.DriveZone.DriveZone.models.Cliente;
import com.DriveZone.DriveZone.utils.JWTUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

/**
 * Controlador REST para gestionar clientes en el sistema.
 */
@RestController
@RequestMapping("/clientes")
public class ClienteController {
    @Autowired
    private ClienteDao clienteDao;

    @Autowired
    private JWTUtil jwtUtil;

    @GetMapping
    public List<Cliente> getClientes() {
        return clienteDao.findAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Cliente> getClienteById(@PathVariable int id) {
        return clienteDao.findById(id)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PostMapping
    public Cliente createCliente(@RequestBody Cliente cliente) {
        return clienteDao.save(cliente);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Cliente> updateCliente(@PathVariable int id, @RequestBody Cliente cliente) {
        if (clienteDao.existsById(id)) {
            cliente.setIdCliente(id);
            return ResponseEntity.ok(clienteDao.save(cliente));
        }
        return ResponseEntity.notFound().build();
    }

    @PutMapping("/{id}/estado")
public ResponseEntity<?> updateEstado(@RequestHeader(value = "Authorization") String token,
                                      @PathVariable int id,
                                      @RequestBody Cliente cliente) {
    if (!validarToken(token)) {
        return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Acceso no autorizado");
    }

    Optional<Cliente> optionalCliente = clienteDao.findById(id);
    if (optionalCliente.isEmpty()) {
        return ResponseEntity.notFound().build();
    }

    Cliente clienteExistente = optionalCliente.get();

    if (cliente.getEstado() == null) {
        return ResponseEntity.badRequest().body("El estado no puede ser nulo");
    }

    clienteExistente.setEstado(cliente.getEstado());

    try {
        clienteDao.save(clienteExistente);
        return ResponseEntity.ok(clienteExistente);
    } catch (Exception e) {
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error al actualizar el estado");
    }
}
    private boolean validarToken(String token) {
        String usuarioId = jwtUtil.getKey(token);
        return usuarioId != null;
    }
}
