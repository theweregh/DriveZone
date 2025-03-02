package com.DriveZone.DriveZone.Expedir_factura.Modelo;

import java.time.LocalDate;
import java.util.List;
import javax.persistence.*;

@Entity
public class Factura {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    @ManyToOne
    private Empresa empresa;
    @ManyToOne
    private Cliente cliente;
    private LocalDate fecha;
    @OneToMany
    private List<ItemFactura> items;
    private double subtotal;
    private double descuentoTotal;
    private double impuestos;
    private double total;
    private String metodoPago;
    
  
    
    public void calcularTotales() {
        subtotal = items.stream().mapToDouble(ItemFactura::calcularSubtotal).sum();
        descuentoTotal = items.stream().mapToDouble(item -> item.getCantidad() * item.getPrecio() * (item.getDescuento() / 100)).sum();
        impuestos = (subtotal - descuentoTotal) * 0.19; // IVA 19%
        total = (subtotal - descuentoTotal) + impuestos;
    }



    public int getId() {
        return id;
    }



    public void setId(int id) {
        this.id = id;
    }



    public Empresa getEmpresa() {
        return empresa;
    }



    public void setEmpresa(Empresa empresa) {
        this.empresa = empresa;
    }



    public Cliente getCliente() {
        return cliente;
    }



    public void setCliente(Cliente cliente) {
        this.cliente = cliente;
    }



    public LocalDate getFecha() {
        return fecha;
    }



    public void setFecha(LocalDate fecha) {
        this.fecha = fecha;
    }



    public List<ItemFactura> getItems() {
        return items;
    }



    public void setItems(List<ItemFactura> items) {
        this.items = items;
    }



    public double getSubtotal() {
        return subtotal;
    }



    public void setSubtotal(double subtotal) {
        this.subtotal = subtotal;
    }



    public double getDescuentoTotal() {
        return descuentoTotal;
    }



    public void setDescuentoTotal(double descuentoTotal) {
        this.descuentoTotal = descuentoTotal;
    }



    public double getImpuestos() {
        return impuestos;
    }



    public void setImpuestos(double impuestos) {
        this.impuestos = impuestos;
    }



    public double getTotal() {
        return total;
    }



    public void setTotal(double total) {
        this.total = total;
    }



    public String getMetodoPago() {
        return metodoPago;
    }



    public void setMetodoPago(String metodoPago) {
        this.metodoPago = metodoPago;
    }



}
