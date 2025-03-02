package com.DriveZone.DriveZone.Buscar_cliente.Controlador;
import java.sql.SQLException;
import java.util.List;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.DriveZone.DriveZone.Buscar_cliente.Modelo.Cliente;


public class ClienteController {
    private ClienteDAO clienteDAO;

    public ClienteController() throws SQLException {
        this.clienteDAO = new ClienteDAO();
    }


     @GetMapping
    public List<Cliente> buscarCliente(@RequestParam String criterio) throws SQLException {
        return clienteDAO.buscarClientes(criterio);
    }
}

