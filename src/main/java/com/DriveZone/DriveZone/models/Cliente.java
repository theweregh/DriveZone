package com.DriveZone.DriveZone.models;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Representa un cliente en el sistema.
 *
 * <p>Esta entidad almacena la información personal de los clientes que realizan compras.</p>
 */
@Entity
@Table(name = "Cliente")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Cliente {
    /**
     * Identificador único del cliente
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "idCliente")
    private int idCliente;

    /**
     * Nombre del cliente
     */
    @Column(nullable = false, length = 100)
    private String nombre;

    /**
     * Apellido del cliente
     */
    @Column(nullable = false, length = 100)
    private String apellido;

    /**
     * Número de cédula del cliente
     */
    @Column(nullable = false, length = 45, unique = true)
    private String cedula;

    /**
     * Dirección del cliente
     */
    @Column(nullable = false, length = 45)
    private String direccion;

    /**
     * Número de teléfono del cliente
     */
    @Column(nullable = false, length = 45)
    private String telefono;

    /**
 * Estado del cliente (activo, inactivo o fuera de servicio)
 */
@Enumerated(EnumType.STRING)
@Column(nullable = false)
private EstadoCliente estado;

    /**
     * Relación con OrdenCompra (puede ser null)
     */
    @ManyToOne
    @JoinColumn(name = "OrdenCompra_idOrdenCompra", referencedColumnName = "idOrdenCompra")
    private OrdenCompra ordenCompra;

}
