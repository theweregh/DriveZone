package com.DriveZone.DriveZone.models;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;

/**
 * Representa una orden de compra en el sistema.
 * <p>
 * Esta entidad almacena la información de una compra realizada, incluyendo el vendedor,
 * los datos de la empresa, la fecha de la compra, los montos asociados y la relación con un carrito de compras.
 * </p>
 *
 * <h2>Ejemplo de uso:</h2>
 * <pre>
 *     OrdenCompra orden = new OrdenCompra();
 *     orden.setVendedor("Carlos Pérez");
 *     orden.setSubtotal(150.0);
 *     orden.setImpuesto(18.0);
 *     orden.setTotal(168.0);
 * </pre>
 *
 * @author DriveZone Team
 * @version 1.0
 * @since 2025-03-08
 */
@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class OrdenCompra {
    /**
     * Identificador único de la orden de compra.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "idOrdenCompra")
    private int id;
    /**
     * Nombre del vendedor que realizó la transacción.
     */
    @Column(name = "vendedor")
    private String vendedor;
    /**
     * Información sobre la empresa que realiza la venta.
     */
    @Column(name = "datosEmpresa")
    private String datosEmpresa;
    /**
     * Fecha y hora en que se realizó la compra.
     */
    @Temporal(TemporalType.TIMESTAMP) // Especifica que es un campo de fecha
    @Column(name = "fecha")
    private Date fecha;
    /**
     * Subtotal de la compra antes de impuestos y descuentos.
     */
    @Column(name = "subtotal")
    private Double subtotal;
    /**
     * Descuento aplicado a la compra.
     */
    @Column(name = "descuento")
    private Double descuento;
    /**
     * Impuesto aplicado a la compra.
     */
    @Column(name = "impuesto")
    private Double impuesto;
    /**
     * Total a pagar después de aplicar descuentos e impuestos.
     */
    @Column(name = "total")
    private Double total;
    /**
     * Relación con el carrito de compras asociado a esta orden.
     */
    @ManyToOne
    @JoinColumn(name = "CarritoCompra_idCarritoCompra", nullable = false)
    private CarritoCompra carritoCompra;

}
