package com.DriveZone.DriveZone.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Representa un carrito de compras en el sistema.
 *
 * <p>Esta entidad almacena los productos añadidos por un usuario al carrito de compras,
 * vinculando los accesorios y la cantidad seleccionada.</p>
 */
@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class CarritoCompra {
    /**
     * Cantidad del accesorio en el carrito
     */
    @Column(name = "cantidad", nullable = true)
    int cantidad;
    /**
     * Identificador único del carrito de compras
     */

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "idCarritoCompra")
    private int id;
    /**
     * Accesorio asociado en el carrito
     */
    @ManyToOne
    @JoinColumn(name = "Accesorio_idAccesorio", nullable = true) // Permite valores nulos
    private Accesorio accesorio;
    /**
     * Usuario propietario del carrito de compras
     */
    @ManyToOne
    @JoinColumn(name = "id_usuario", nullable = false)
    @JsonIgnore
    private Usuario usuario;
}
