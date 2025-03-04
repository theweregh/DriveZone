package com.DriveZone.DriveZone.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Entity
@Table(name = "usuario")
@Getter @Setter
@AllArgsConstructor @NoArgsConstructor
public class Usuario {
    // Identificador, nombre de usuario, nombres y apellidos, cédula, Correo, dirección y teléfono.
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "idUsuario")
    private int id;

    @Column(name = "username")
    private String username;

    @Column(name = "nombreRazonSocial")
    private String nombres;

    @Column(name = "identificacion")
    private String cedula;

     @Column(name = "correo")
    private String correo;

    @Column(name = "direccion")
    private String direccion;

    @Column(name = "telefono")
    private String telefono;

    @Column(name = "password")
    private String password;

    @Column(name = "estado")
    private String estado;

    @Column(name = "rol")
    private String rol;

    //@OneToMany(mappedBy = "usuario")
    @OneToMany(mappedBy = "usuario", cascade = CascadeType.ALL)
    @JsonIgnore
    private List<CarritoCompra> carritos;

    public Usuario(int usuarioId) {
        this.id = usuarioId;
    }
}

