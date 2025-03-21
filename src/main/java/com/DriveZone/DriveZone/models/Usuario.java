package com.DriveZone.DriveZone.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

/**
 * Representa un usuario dentro del sistema.
 */
@Entity
@Table(name = "usuario")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Usuario {
    /**
     * Identificador único del usuario.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "idUsuario")
    private int id;
    /**
     * Nombre de usuario para iniciar sesión.
     */
    @Column(name = "username")
    private String username;
    /**
     * Nombres completos o razón social del usuario.
     */
    @Column(name = "nombreRazonSocial")
    private String nombres;
    /**
     * Número de identificación o cédula del usuario.
     */
    @Column(name = "identificacion")
    private String cedula;
    /**
     * Correo electrónico del usuario.
     */
    @Column(name = "correo")
    private String correo;
    /**
     * Dirección del usuario.
     */
    @Column(name = "direccion")
    private String direccion;
    /**
     * Número de teléfono del usuario.
     */
    @Column(name = "telefono")
    private String telefono;
    /**
     * Contraseña del usuario (debe manejarse con seguridad).
     */
    @Column(name = "password")
    private String password;
    /**
     * Estado del usuario (ejemplo: activo o inactivo).
     */
    @Column(name = "estado")
    private String estado;
    /**
     * Rol del usuario dentro del sistema.
     */
    @Column(name = "rol")
    private String rol;
    /**
     * Constructor que permite crear un usuario con un identificador específico.
     *
     * @param usuarioId ID del usuario.
     */
    public Usuario(int usuarioId) {
        this.id = usuarioId;
    }
}

