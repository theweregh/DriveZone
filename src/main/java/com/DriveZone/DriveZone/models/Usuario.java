package com.DriveZone.DriveZone.models;

import jakarta.persistence.*;

@Entity
@Table(name = "usuario")
public class Usuario {
    // Identificador, nombre de usuario, nombres y apellidos, cédula, Correo, dirección y teléfono.
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private int id;

    @Column(name = "username")
    private String username;

    @Column(name = "nombre")
    private String nombres;

    @Column(name = "cedula")
    private String cedula;

     @Column(name = "correo")
    private String correo;

    @Column(name = "direccion")
    private String direccion;

    @Column(name = "telefono")
    private String telefono;

    @Column(name = "password")
    private String password;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getNombres() {
        return nombres;
    }

    public void setNombres(String nombres) {
        this.nombres = nombres;
    }

    public String getCedula() {
        return cedula;
    }

    public void setCedula(String cedula) {
        this.cedula = cedula;
    }

    public String getCorreo() {
        return correo;
    }

    public void setCorreo(String correo) {
        this.correo = correo;
    }

    public String getDireccion() {
        return direccion;
    }

    public void setDireccion(String direccion) {
        this.direccion = direccion;
    }

    public String getTelefono() {
        return telefono;
    }

    public void setTelefono(String telefono) {
        this.telefono = telefono;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
    public Usuario() {
    }
    public Usuario(int id, String username, String nombres, String cedula, String correo, String direccion, String telefono, String password) {
        this.id = id;
        this.username = username;
        this.nombres = nombres;
        this.cedula = cedula;
        this.correo = correo;
        this.direccion = direccion;
        this.telefono = telefono;
        this.password = password;
    }
}
