package com.DriveZone.DriveZone.models;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;

/**
 * Representa una factura generada en el sistema.
 *
 * <p>Una factura está asociada a una orden de compra e incluye información
 * sobre la empresa emisora, el método de pago y los cálculos financieros
 * como subtotal, impuestos y descuentos.</p>
 */
@Entity
@Table(name = "factura")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Factura {
    /**
     * Identificador único de la factura.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer idFactura;
    /**
     * Nombre de la empresa emisora de la factura.
     */
    private String empresaNombre;
    /**
     * Número de Identificación Tributaria (NIT) de la empresa emisora.
     */
    private String nit;
    /**
     * Dirección de la empresa emisora de la factura.
     */
    private String direccion;
    /**
     * Método de pago utilizado en la transacción (ejemplo: efectivo, tarjeta, transferencia).
     */
    private String metodoPago;
    /**
     * Fecha en la que se generó la factura.
     */
    private Date fecha;
    /**
     * Subtotal de la compra antes de aplicar descuentos e impuestos.
     */
    private double subtotal;
    /**
     * Descuento aplicado a la compra.
     */
    private double descuento;
    /**
     * Impuestos aplicados a la compra.
     */
    private double impuestos;
    /**
     * Total a pagar después de aplicar descuentos e impuestos.
     */
    private double total;
    /**
     * Orden de compra asociada a la factura.
     */
    @ManyToOne
    @JoinColumn(name = "ordencompra_idOrdenCompra", nullable = false)
    private OrdenCompra ordenCompra;
}
