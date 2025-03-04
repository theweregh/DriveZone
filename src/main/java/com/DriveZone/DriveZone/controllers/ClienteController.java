// ClienteController.java - Controlador REST para Cliente
package com.DriveZone.DriveZone.controllers;

import com.DriveZone.DriveZone.models.Cliente;

import de.mkammerer.argon2.Argon2;
import de.mkammerer.argon2.Argon2Factory;

import com.DriveZone.DriveZone.utils.JWTUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

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

    @GetMapping("/buscar")
    public List<Cliente> buscarClientes(@RequestParam String criterio) {
        return clienteRepository.findByNombreContainingIgnoreCaseOrApellidoContainingIgnoreCaseOrCedula(criterio, criterio, criterio);
    }

    @PostMapping("/registro")
    public void registrarCliente(@RequestBody Cliente cliente) {
        clienteRepository.save(cliente);
    }

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

    @GetMapping("/{id}")
    public Cliente obtenerClientePorId(@PathVariable Long id) {
        return clienteRepository.findById(id).orElse(null);
    }

    @PutMapping("/{id}")
    public Cliente actualizarCliente(@PathVariable Long id, @RequestBody Cliente cliente) {
        if (clienteRepository.existsById(id)) {
            cliente.setIdCliente(id);
            return clienteRepository.save(cliente);
        }
        return null;
    }

    @DeleteMapping("/{id}")
    public void eliminarCliente(@PathVariable Long id) {
        clienteRepository.deleteById(id);
    }
}
