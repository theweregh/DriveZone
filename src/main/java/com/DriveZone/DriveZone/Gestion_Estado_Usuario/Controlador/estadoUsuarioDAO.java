package com.DriveZone.DriveZone.Gestion_Estado_Usuario.Controlador;

import com.DriveZone.DriveZone.Gestion_Estado_Usuario.Modelo.UsuarioEstado;
import com.DriveZone.DriveZone.Gestion_Estado_Usuario.Modelo.EstadoUsuario;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class estadoUsuarioDAO {
    /*private Connection conexion;

    public estadoUsuarioDAO() throws SQLException {
        String url = "jdbc:mysql://localhost:3306/DriveZone";
        String usuario = "root";
        String clave = "Jesusd@1346";
        conexion = DriverManager.getConnection(url, usuario, clave);
    }

    public List<UsuarioEstado> buscarUsuarios(String criterio) throws SQLException {
        List<UsuarioEstado> usuarios = new ArrayList<>();
        String sql = "SELECT * FROM Usuario WHERE LOWER(nombreRazonSocial) LIKE ? OR identificacion = ?";
        try (PreparedStatement stmt = conexion.prepareStatement(sql)) {
            stmt.setString(1, "%" + criterio.toLowerCase() + "%");
            stmt.setString(2, criterio);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                UsuarioEstado usuario = new UsuarioEstado(
                    rs.getInt("idUsuario"),
                    rs.getString("username"),
                    rs.getString("nombreRazonSocial"),
                    rs.getString("identificacion"),
                    rs.getString("correo"),
                    rs.getString("direccion"),
                    rs.getString("telefono"),
                    rs.getString("password"),
                    rs.getString("rol"),
                    EstadoUsuario.valueOf(rs.getString("estado").toUpperCase())
                );
                usuarios.add(usuario);
            }
        }
        return usuarios;
    }

    public boolean actualizarEstado(int id, String nuevoEstado) throws SQLException {
        String sql = "UPDATE Usuario SET estado = ? WHERE idUsuario = ?";
        try (PreparedStatement stmt = conexion.prepareStatement(sql)) {
            stmt.setString(1, nuevoEstado.toUpperCase());
            stmt.setInt(2, id);
            int filasActualizadas = stmt.executeUpdate();
            return filasActualizadas > 0;
        }
    }*/
}
