package com.DriveZone.DriveZone.models;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Representa un accesorio en el sistema.
 *
 * <p>Esta entidad se mapea a la tabla "Accesorio" en la base de datos
 * e incluye información relevante sobre el producto, como nombre, stock,
 * precio, imagen y descuentos aplicables.</p>
 */
@Entity
@Table(name = "Accesorio")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Accesorio {
    /**
     * Nombre del accesorio
     */
    @Column(name = "nombre")
    String nombre;
    /**
     * Descripción del accesorio
     */
    @Column(name = "descripcion")
    String descripcion;
    /**
     * Identificador único del accesorio
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "idAccesorio")
    private int id;
    /**
     * Cantidad disponible en stock
     */
    @Column(name = "stock")
    private int stock;
    /**
     * Precio de venta del accesorio
     */
    @Column(name = "precioVenta")
    private double precioVenta;
    /**
     * URL de la imagen del accesorio
     */
    @Column(name = "imagen")
    private String imagen;
    /**
     * Descuento aplicado al accesorio (en porcentaje)
     */
    @Column(name = "descuento")
    private double descuento;
    
}
