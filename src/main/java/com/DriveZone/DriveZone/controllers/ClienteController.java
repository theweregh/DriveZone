package com.DriveZone.DriveZone.controllers;

import com.DriveZone.DriveZone.models.Cliente;
import com.DriveZone.DriveZone.repository.ClienteRepository;
import com.DriveZone.DriveZone.utils.JWTUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Controlador REST para gestionar clientes en el sistema.
 */
@RestController
@RequestMapping("/clientes")
@CrossOrigin(origins = "*")
public class ClienteController {

    @Autowired
    private ClienteRepository clienteRepository;
    @Autowired
    private JWTUtil jwtUtil;

    @GetMapping
    public List<Cliente> obtenerClientes() {
        return clienteRepository.findAll();
    }

    /**
     * Busca clientes en la base de datos según un criterio (nombre, apellido o cédula).
     *
     * @param criterio Texto a buscar en los campos nombre, apellido o cédula.
     * @return Lista de clientes coincidentes con el criterio de búsqueda.
     */
    @GetMapping("/buscar")
    public List<Cliente> buscarClientes(@RequestParam String criterio) {
        return clienteRepository.findByNombreContainingIgnoreCaseOrApellidoContainingIgnoreCaseOrCedula(criterio, criterio, criterio);
    }

    /**
     * Registra un nuevo cliente en la base de datos.
     *
     * @param cliente Objeto {@link Cliente} a registrar.
     */
    @PostMapping("/registro")
    public void registrarCliente(@RequestBody Cliente cliente) {
        clienteRepository.save(cliente);
    }

    /**
     * Autentica a un cliente con su nombre de usuario y contraseña.
     *
     * @param username Nombre de usuario del cliente.
     * @param password Contraseña del cliente (se compara con la cédula en este caso).
     * @return Token JWT si la autenticación es exitosa, o un mensaje de error en caso contrario.
     */
    @PostMapping("/login")
    public String autenticarCliente(@RequestParam String username, @RequestParam String password) {
        List<Cliente> lista = clienteRepository.findByNombre(username);
        if (lista.isEmpty()) {
            return "Usuario no encontrado";
        }
        Cliente cliente = lista.get(0);
        if (cliente.getCedula().equals(password)) {
            return jwtUtil.create(String.valueOf(cliente.getIdCliente()), cliente.getNombre());
        }
        return "Credenciales incorrectas";
    }

    /**
     * Obtiene un cliente específico por su ID.
     *
     * @param id ID del cliente a buscar.
     * @return Objeto {@link Cliente} si se encuentra, o `null` si no existe.
     */
    @GetMapping("/{id}")
    public Cliente obtenerClientePorId(@PathVariable Long id) {
        return clienteRepository.findById(id).orElse(null);
    }

    /**
     * Actualiza la información de un cliente existente.
     *
     * @param id      ID del cliente a actualizar.
     * @param cliente Objeto {@link Cliente} con la información actualizada.
     * @return Cliente actualizado o `null` si no se encuentra en la base de datos.
     */
    @PutMapping("/{id}")
    public Cliente actualizarCliente(@PathVariable Long id, @RequestBody Cliente cliente) {
        if (clienteRepository.existsById(id)) {
            cliente.setIdCliente(id);
            return clienteRepository.save(cliente);
        }
        return null;
    }

    /**
     * Elimina un cliente de la base de datos.
     *
     * @param id ID del cliente a eliminar.
     */
    @DeleteMapping("/{id}")
    public void eliminarCliente(@PathVariable Long id) {
        clienteRepository.deleteById(id);
    }
}
