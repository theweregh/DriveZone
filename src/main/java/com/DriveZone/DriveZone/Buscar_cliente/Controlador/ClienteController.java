package com.DriveZone.DriveZone.Buscar_cliente.Controlador;

import com.DriveZone.DriveZone.Buscar_cliente.Modelo.Cliente;
import org.springframework.web.bind.annotation.*;

import java.sql.SQLException;
import java.util.List;

@RestController
@RequestMapping("/clientes")
@CrossOrigin(origins = "*")  // Permite llamadas desde JavaScript en el navegador
public class ClienteController {

    private final ClienteDAO clienteDAO;

    public ClienteController() throws SQLException {
        this.clienteDAO = new ClienteDAO();
    }

    @GetMapping
    public List<Cliente> obtenerClientes() throws SQLException {
        return clienteDAO.buscarClientes(""); // Retorna todos los clientes
    }

    @GetMapping("/buscar")
    public List<Cliente> buscarClientes(@RequestParam String criterio) throws SQLException {
        return clienteDAO.buscarClientes(criterio); // Retorna clientes filtrados
    }
}
