package com.DriveZone.DriveZone.Expedir_factura.Modelo;

import javax.persistence.*;

@Entity
public class ItemFactura {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String descripcion;
    private int cantidad;
    private double precio;
    private double descuento;

    
    public ItemFactura(String descripcion, int cantidad, double precio, double descuento) {
        this.descripcion = descripcion;
        this.cantidad = cantidad;
        this.precio = precio;
        this.descuento = descuento;
    }
    

    public double calcularSubtotal() {
        return cantidad * precio * (1 - descuento / 100);
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public int getCantidad() {
        return cantidad;
    }

    public void setCantidad(int cantidad) {
        this.cantidad = cantidad;
    }

    public double getPrecio() {
        return precio;
    }

    public void setPrecio(double precio) {
        this.precio = precio;
    }

    public double getDescuento() {
        return descuento;
    }

    public void setDescuento(double descuento) {
        this.descuento = descuento;
    }
    
    

}

