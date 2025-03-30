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
 * los datos de la empresa, la fecha de la compra, los montos asociados y la relación con un usuario y un cliente.
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
    private int idOrdenCompra;
    /**
     * Nombre del vendedor que realizó la transacción.
     */
    @Column(name = "vendedor", nullable = false, length = 45)
    private String vendedor;
    /**
     * Información de la empresa asociada a la compra.
     */
    @Column(name = "datosEmpresa", nullable = false, length = 45)
    private String datosEmpresa;
    /**
     * Fecha en la que se realizó la compra.
     */
    @Column(name = "fecha", nullable = false)
    @Temporal(TemporalType.DATE)
    private Date fecha;
    /**
     * Precio de venta de los productos en la orden de compra.
     */
    @Column(name = "precioVenta", nullable = false)
    private Double precioVenta;
    /**
     * Subtotal de la orden antes de aplicar impuestos y descuentos.
     */
    @Column(name = "subtotal", nullable = false)
    private Double subtotal;
    /**
     * Descuento aplicado a la orden de compra.
     */
    @Column(name = "descuento", nullable = false)
    private Double descuento;
    /**
     * Impuesto aplicado a la orden de compra.
     */
    @Column(name = "impuesto", nullable = false)
    private Double impuesto;
    /**
     * Total final de la orden de compra después de aplicar impuestos y descuentos.
     */
    @Column(name = "total", nullable = false)
    private Double total;
    /**
     * Relación con la entidad {@link Usuario}, que representa al usuario que realizó la compra.
     */
    @ManyToOne
    @JoinColumn(name = "Usuario_idUsuario")
    private Usuario usuario;
    /**
     * Relación con la entidad {@link Cliente}, que representa al cliente que realizó la compra.
     */
    @ManyToOne
    @JoinColumn(name = "Cliente_idCliente")
    private Cliente cliente;

}
