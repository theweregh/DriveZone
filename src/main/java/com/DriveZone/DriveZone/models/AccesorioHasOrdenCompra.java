package com.DriveZone.DriveZone.models;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Representa la relación entre {@link Accesorio} y {@link OrdenCompra}.
 *
 * <p>Esta clase gestiona la relación muchos a muchos entre accesorios y órdenes
 * de compra, permitiendo registrar la cantidad de un accesorio en una orden específica.</p>
 *
 * <p>Se utiliza una clave primaria compuesta a través de {@link AccesorioHasOrdenCompraId}.</p>
 *
 * @author DriveZone
 */
@Entity
@Table(name = "accesorio_has_ordencompra")
@Getter
@Setter
@NoArgsConstructor
public class AccesorioHasOrdenCompra {
    /**
     * Identificador compuesto que representa la relación entre un accesorio y una orden de compra.
     */
    @EmbeddedId
    private AccesorioHasOrdenCompraId id;
    /**
     * Referencia al accesorio asociado en la orden de compra.
     */
    @ManyToOne
    @MapsId("idAccesorio") // Relaciona con la clave primaria compuesta
    @JoinColumn(name = "accesorio_idAccesorio", nullable = false)
    private Accesorio accesorio;
    /**
     * Referencia a la orden de compra en la que se incluye el accesorio.
     */
    @ManyToOne
    @MapsId("idOrdenCompra") // Relaciona con la clave primaria compuesta
    @JoinColumn(name = "ordencompra_idOrdenCompra", nullable = false)
    private OrdenCompra ordenCompra;
    /**
     * Cantidad del accesorio solicitada en la orden de compra.
     */
    @Column(name = "cantidad", nullable = false)
    private int cantidad;
}
