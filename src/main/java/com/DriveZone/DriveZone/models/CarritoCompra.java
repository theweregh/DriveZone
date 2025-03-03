package com.DriveZone.DriveZone.models;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class CarritoCompra {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "idCarritoCompra")
    private int id;
    @Column(name = "cantidad", nullable = false)
    int cantidad;
    @ManyToOne
    @JoinColumn(name = "orden_compra_id", referencedColumnName = "idOrdenCompra")
    private OrdenCompra ordenCompra;
    @ManyToOne
    @JoinColumn(name = "Accesorio_idAccesorio", nullable = false)
    private Accesorio accesorio;
    @ManyToOne
    @JoinColumn(name = "usuario_id", nullable = false)
    private Usuario usuario; // Relación con Usuario
}
