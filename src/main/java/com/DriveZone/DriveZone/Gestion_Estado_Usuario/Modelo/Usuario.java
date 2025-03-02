package com.DriveZone.DriveZone.Gestion_Estado_Usuario.Modelo;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

import com.DriveZone.DriveZone.Gestion_Estado_Usuario.Controlador.ConexionDB;


public class Usuario {
    private int id;
    private String nombreUsuario;
    private String nombres;
    private String apellidos;
    private String cedula;
    private String correo;
    private String direccion;
    private String telefono;
    private String contraseña;
    private String rol;
    private EstadoUsuario estado;

    // Constructor
    public Usuario(int id, String nombreUsuario, String nombres, String apellidos, String cedula, String correo, String direccion, String telefono, String contraseña, String rol, EstadoUsuario estado) {
        this.id = id;
        this.nombreUsuario = nombreUsuario;
        this.nombres = nombres;
        this.apellidos = apellidos;
        this.cedula = cedula;
        this.correo = correo;
        this.direccion = direccion;
        this.telefono = telefono;
        this.contraseña = contraseña;
        this.rol = rol;
        this.estado = estado;
    }

    // Getters y Setters
    public int getId() { return id; }
    public String getNombreUsuario() { return nombreUsuario; }
    public String getNombres() { return nombres; }
    public String getApellidos() { return apellidos; }
    public String getCedula() { return cedula; }
    public String getCorreo() { return correo; }
    public String getDireccion() { return direccion; }
    public String getTelefono() { return telefono; }
    public String getContraseña() { return contraseña; }
    public String getRol() { return rol; }
    public EstadoUsuario getEstado() { return estado; }

    // Método para cargar todos los usuarios desde la base de datos
    public static List<Usuario> cargarUsuariosDesdeDB() {
        List<Usuario> usuarios = new ArrayList<>();
        String query = "SELECT * FROM Usuarios";

        try (Connection conn = ConexionDB.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(query)) {

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
                    EstadoUsuario.valueOf(rs.getString("estado"))
                );
                usuarios.add(usuario);
            }
        } catch (SQLException e) {
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
                    EstadoUsuario.valueOf(rs.getString("estado"))
                );
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    // Método para buscar usuarios por nombre
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
                    EstadoUsuario.valueOf(rs.getString("estado"))
                );
                usuarios.add(usuario);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return usuarios;
    }



    // Método para actualizar el estado en la BD
    public static boolean actualizarEstado(int id, EstadoUsuario nuevoEstado) {
        String query = "UPDATE Usuarios SET estado = ? WHERE id = ?";

        try (Connection conn = ConexionDB.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query)) {
            
            pstmt.setString(1, nuevoEstado.toString());
            pstmt.setInt(2, id);
            int filasActualizadas = pstmt.executeUpdate();

            return filasActualizadas > 0;
        } catch (SQLException e) {
            System.err.println("Error al actualizar estado: " + e.getMessage());
            return false;
        }
    }
    
}