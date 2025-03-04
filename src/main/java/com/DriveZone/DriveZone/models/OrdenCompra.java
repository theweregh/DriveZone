package com.DriveZone.DriveZone.models;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
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

    @Column(name = "subtotal")
    private Double subtotal;

    @Column(name = "descuento")
    private Double descuento;

    @Column(name = "impuesto")
    private Double impuesto;

    @Column(name = "total")
    private Double total;

    @ManyToOne
    @JoinColumn(name = "CarritoCompra_idCarritoCompra", nullable = false)
    private CarritoCompra carritoCompra;

}
