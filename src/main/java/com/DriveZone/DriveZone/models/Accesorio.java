package com.DriveZone.DriveZone.models;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "Accesorio")
@Getter @Setter
@AllArgsConstructor @NoArgsConstructor
public class Accesorio {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    int id;
    @Column(name = "nombre")
    String nombre;
    @Column(name = "descripcion")
    String descripcion;
    @Column(name = "stock")
    int stock;
    @Column(name = "precioVenta")
    double precioVenta;
    @Column(name = "imagen")
    String imagen;
}
