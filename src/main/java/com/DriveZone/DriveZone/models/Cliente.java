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
    private Long idCliente;
    /**
     * Nombre del cliente
     */
    private String nombre;
    /**
     * Apellido del cliente
     */
    private String apellido;
    /**
     * Número de cédula del cliente
     */
    private String cedula;
    /**
     * Dirección del cliente
     */
    private String direccion;
    /**
     * Número de teléfono del cliente
     */
    private String telefono;
    /**
     * Estado del cliente (activo o inactivo)
     */
    private boolean estado;

}
