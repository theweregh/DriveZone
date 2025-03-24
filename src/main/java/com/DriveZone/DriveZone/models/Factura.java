package com.DriveZone.DriveZone.models;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;

@Entity
@Table(name = "factura")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Factura {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer idFactura;
    private String empresaNombre;
    private String nit;
    private String direccion;
    private String metodoPago;
    private Date fecha;
    private double subtotal;
    private double descuento;
    private double impuestos;
    private double total;

    @ManyToOne
    @JoinColumn(name = "ordencompra_idOrdenCompra", nullable = false)
    private OrdenCompra ordenCompra;
}
