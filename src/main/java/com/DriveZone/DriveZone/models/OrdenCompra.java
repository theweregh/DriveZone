package com.DriveZone.DriveZone.models;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;
import java.util.List;

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
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "idOrdenCompra")
    private Integer idOrdenCompra;

    @Column(name = "vendedor", nullable = false, length = 45)
    private String vendedor;

    @Column(name = "datosEmpresa", nullable = false, length = 45)
    private String datosEmpresa;

    @Column(name = "fecha", nullable = false)
    @Temporal(TemporalType.DATE)
    private Date fecha;

    @Column(name = "precioVenta", nullable = false)
    private Double precioVenta;

    @Column(name = "subtotal", nullable = false)
    private Double subtotal;

    @Column(name = "descuento", nullable = false)
    private Double descuento;

    @Column(name = "impuesto", nullable = false)
    private Double impuesto;

    @Column(name = "total", nullable = false)
    private Double total;
    @ManyToOne
    @JoinColumn(name = "Usuario_idUsuario")
    private Usuario usuario;
    @ManyToOne
    @JoinColumn(name = "Cliente_idCliente")
    private Cliente cliente;
}
