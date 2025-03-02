package com.DriveZone.DriveZone.Buscar_cliente.Controlador;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

import com.DriveZone.DriveZone.Buscar_cliente.Modelo.Cliente;


public class ClienteDAO {
    private Connection conexion;

    public ClienteDAO() throws SQLException {
        String url = "jdbc:mysql://localhost:3306/cliente";
        String usuario = "root";
        String clave = "Jesusd@1346";
        conexion = DriverManager.getConnection(url, usuario, clave);
    }

    public List<Cliente> buscarClientes(String criterio) throws SQLException {
        List<Cliente> clientes = new ArrayList<>();
        String sql = "SELECT * FROM clientes WHERE LOWER(nombre) LIKE ? OR identificacion = ?";
        try (PreparedStatement stmt = conexion.prepareStatement(sql)) {
            stmt.setString(1, "%" + criterio.toLowerCase() + "%");
            stmt.setString(2, criterio);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                Cliente cliente = new Cliente(
                    rs.getInt("id_cliente"),
                    rs.getString("nombre"),
                    rs.getString("apellido"),
                    rs.getString("identificacion"),
                    rs.getString("direccion"),
                    rs.getString("telefono"),
                    rs.getBoolean("estado")
                );
                clientes.add(cliente);
            }
        }
        return clientes;
    }
}