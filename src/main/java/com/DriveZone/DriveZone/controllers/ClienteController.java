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
 * Proporciona endpoints para listar, crear, actualizar y modificar el estado de los clientes.
 */
@RestController
@RequestMapping("/clientes")
public class ClienteController {
    @Autowired
    private ClienteDao clienteDao;

    @Autowired
    private JWTUtil jwtUtil;

    /**
     * Obtiene la lista de todos los clientes registrados en el sistema.
     *
     * @return Lista de objetos {@link Cliente}.
     */
    @GetMapping
    public List<Cliente> getClientes() {
        return clienteDao.findAll();
    }

    /**
     * Obtiene un cliente por su ID.
     *
     * @param id Identificador del cliente.
     * @return El cliente si se encuentra, de lo contrario, una respuesta con código HTTP 404 (Not Found).
     */
    @GetMapping("/{id}")
    public ResponseEntity<Cliente> getClienteById(@PathVariable int id) {
        return clienteDao.findById(id)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    /**
     * Crea un nuevo cliente en el sistema.
     *
     * @param cliente Objeto {@link Cliente} con la información del cliente a registrar.
     * @return El cliente registrado.
     */
    @PostMapping
    public Cliente createCliente(@RequestBody Cliente cliente) {
        return clienteDao.save(cliente);
    }

    /**
     * Actualiza la información de un cliente existente.
     *
     * @param id      Identificador del cliente a actualizar.
     * @param cliente Objeto {@link Cliente} con los nuevos datos.
     * @return El cliente actualizado si existe, de lo contrario, una respuesta con código HTTP 404 (Not Found).
     */
    @PutMapping("/{id}")
    public ResponseEntity<Cliente> updateCliente(@PathVariable int id, @RequestBody Cliente cliente) {
        if (clienteDao.existsById(id)) {
            cliente.setIdCliente(id);
            return ResponseEntity.ok(clienteDao.save(cliente));
        }
        return ResponseEntity.notFound().build();
    }

    /**
     * Actualiza el estado de un cliente específico.
     *
     * @param token   Token de autorización en la cabecera de la solicitud.
     * @param id      Identificador del cliente cuyo estado se va a actualizar.
     * @param cliente Objeto {@link Cliente} con el nuevo estado.
     * @return El cliente con el estado actualizado si la operación es exitosa,
     * una respuesta con código HTTP 403 (Forbidden) si el token no es válido,
     * una respuesta con código HTTP 400 (Bad Request) si el estado es nulo,
     * o una respuesta con código HTTP 500 (Internal Server Error) en caso de fallo en la actualización.
     */
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

    /**
     * Valida el token de autenticación verificando si es válido y contiene un ID de usuario.
     *
     * @param token Token JWT a validar.
     * @return {@code true} si el token es válido, {@code false} en caso contrario.
     */
    private boolean validarToken(String token) {
        String usuarioId = jwtUtil.getKey(token);
        return usuarioId != null;
    }
}
