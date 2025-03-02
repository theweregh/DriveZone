package com.DriveZone.DriveZone.Gestion_Estado_Usuario.Controlador;



import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.DriveZone.DriveZone.Gestion_Estado_Usuario.Modelo.EstadoUsuario;
import com.DriveZone.DriveZone.Gestion_Estado_Usuario.Modelo.Usuario;

public class UsuarioController {

    // Método para cargar todos los usuarios desde la base de datos
    public static List<Usuario> cargarUsuariosDesdeDB() {
        List<Usuario> usuarios = new ArrayList<>();
        String query = "SELECT * FROM Usuarios";

        try (Connection conn = ConexionDB.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query);
             ResultSet rs = pstmt.executeQuery()) {

            while (rs.next()) {
                String estadoDB = rs.getString("estado").toUpperCase().trim(); // Normaliza el estado

                Usuario usuario = new Usuario(
                    rs.getInt("id"),
                    rs.getString("nombre_usuario"),
                    rs.getString("nombres"),
                    rs.getString("apellidos"),
                    rs.getString("cedula"),
                    rs.getString("correo"),
                    rs.getString("direccion"),
                    rs.getString("telefono"),
                    rs.getString("contraseña"),
                    rs.getString("rol"),
                    EstadoUsuario.valueOf(estadoDB) // Convierte el estado normalizado
                );
                usuarios.add(usuario);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (IllegalArgumentException e) {
            System.err.println("Error: Estado no válido en la base de datos.");
            e.printStackTrace();
        }
        return usuarios;
    }

    // Método para buscar un usuario por ID
    public static Usuario buscarUsuarioPorId(int id) {
        String query = "SELECT * FROM Usuarios WHERE id = ?";
        try (Connection conn = ConexionDB.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query)) {

            pstmt.setInt(1, id);
            ResultSet rs = pstmt.executeQuery();

            if (rs.next()) {
                return new Usuario(
                    rs.getInt("id"),
                    rs.getString("nombre_usuario"),
                    rs.getString("nombres"),
                    rs.getString("apellidos"),
                    rs.getString("cedula"),
                    rs.getString("correo"),
                    rs.getString("direccion"),
                    rs.getString("telefono"),
                    rs.getString("contraseña"),
                    rs.getString("rol"),
                    EstadoUsuario.valueOf(rs.getString("estado").toUpperCase())
                );
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    // Método para buscar usuarios por nombre o apellido
    public static List<Usuario> buscarUsuariosPorNombre(String nombre) {
        List<Usuario> usuarios = new ArrayList<>();
        String query = "SELECT * FROM Usuarios WHERE nombres LIKE ? OR apellidos LIKE ?";
        try (Connection conn = ConexionDB.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query)) {

            pstmt.setString(1, "%" + nombre + "%");
            pstmt.setString(2, "%" + nombre + "%");
            ResultSet rs = pstmt.executeQuery();

            while (rs.next()) {
                Usuario usuario = new Usuario(
                    rs.getInt("id"),
                    rs.getString("nombre_usuario"),
                    rs.getString("nombres"),
                    rs.getString("apellidos"),
                    rs.getString("cedula"),
                    rs.getString("correo"),
                    rs.getString("direccion"),
                    rs.getString("telefono"),
                    rs.getString("contraseña"),
                    rs.getString("rol"),
                    EstadoUsuario.valueOf(rs.getString("estado").toUpperCase())
                );
                usuarios.add(usuario);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return usuarios;
    }

   // En UsuarioController.java
   public static boolean actualizarEstado(int id, EstadoUsuario nuevoEstado) {
    String query = "UPDATE Usuarios SET estado = ? WHERE id = ?";
    
    try (Connection conn = ConexionDB.getConnection();
         PreparedStatement pstmt = conn.prepareStatement(query)) {

        conn.setAutoCommit(false); // Deshabilitar autocommit
        
        pstmt.setString(1, nuevoEstado.toString());
        pstmt.setInt(2, id);

        int filasActualizadas = pstmt.executeUpdate();
        conn.commit(); // Confirmar transacción

        System.out.println("ID: " + id + " Estado Nuevo: " + nuevoEstado);
        System.out.println("Filas actualizadas: " + filasActualizadas);
        
        return filasActualizadas > 0;
    } catch (SQLException e) {
        System.err.println("Error SQL: " + e.getMessage());
        return false;
    }
}


    
    
}