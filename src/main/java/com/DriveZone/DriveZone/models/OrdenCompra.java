package com.DriveZone.DriveZone.models;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;
import java.util.List;
@Entity
@Getter @Setter
@AllArgsConstructor @NoArgsConstructor
public class OrdenCompra {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "idOrdenCompra")
    private int id;

    @Column(name = "vendedor")
    private String vendedor;

    @Column(name = "datosEmpresa")
    private String datosEmpresa;

    @Temporal(TemporalType.TIMESTAMP) // Especifica que es un campo de fecha
    @Column(name = "fecha")
    private Date fecha;

    // Relación con CarritoCompra (Una orden tiene varios productos en el carrito)
    @OneToMany(mappedBy = "ordenCompra", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<CarritoCompra> carrito;

    @Column(name = "subtotal")
    private Double subtotal;

    @Column(name = "descuento")
    private Double descuento;

    @Column(name = "impuesto")
    private Double impuesto;

    @Column(name = "total")
    private Double total;

    @ManyToOne
    @JoinColumn(name = "usuario_id", nullable = false)
    private Usuario usuario; // Relación con Usuario
}
