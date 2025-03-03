package com.DriveZone.DriveZone.Gestion_Estado_Usuario.Modelo;

import jakarta.persistence.*;

@Entity
@Table(name = "Usuario")
public class UsuarioEstado {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int idUsuario;
    private String username;
    private String nombreRazonSocial;
    private String identificacion;
    private String correo;
    private String direccion;
    private String telefono;
    private String password;
    @Enumerated(EnumType.STRING)
    private EstadoUsuario estado;
    private String rol;

    // Constructor
    public UsuarioEstado(int idUsuario, String username, String nombreRazonSocial, String identificacion, String correo,
                   String direccion, String telefono, String password, String rol, EstadoUsuario estado) {
        this.idUsuario = idUsuario;
        this.username = username;
        this.nombreRazonSocial = nombreRazonSocial;
        this.identificacion = identificacion;
        this.correo = correo;
        this.direccion = direccion;
        this.telefono = telefono;
        this.password = password;
        this.rol = rol;
        this.estado = estado;
    }

    // Getters y Setters
    public int getIdUsuario() { return idUsuario; }
    public void setIdUsuario(int idUsuario) { this.idUsuario = idUsuario; }

    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }

    public String getNombreRazonSocial() { return nombreRazonSocial; }
    public void setNombreRazonSocial(String nombreRazonSocial) { this.nombreRazonSocial = nombreRazonSocial; }

    public String getIdentificacion() { return identificacion; }
    public void setIdentificacion(String identificacion) { this.identificacion = identificacion; }

    public String getCorreo() { return correo; }
    public void setCorreo(String correo) { this.correo = correo; }

    public String getDireccion() { return direccion; }
    public void setDireccion(String direccion) { this.direccion = direccion; }

    public String getTelefono() { return telefono; }
    public void setTelefono(String telefono) { this.telefono = telefono; }

    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }

    public EstadoUsuario getEstado() { return estado; }
    public void setEstado(EstadoUsuario estado) { this.estado = estado; }

    public String getRol() { return rol; }
    public void setRol(String rol) { this.rol = rol; }
}