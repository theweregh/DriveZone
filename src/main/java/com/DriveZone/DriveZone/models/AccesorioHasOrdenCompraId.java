package com.DriveZone.DriveZone.models;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.*;

import java.io.Serializable;

/**
 * Clase que representa la clave primaria compuesta de la entidad {@link AccesorioHasOrdenCompra}.
 *
 * <p>Esta clase se utiliza para definir la relación muchos a muchos entre {@link Accesorio} y {@link OrdenCompra}.
 * Implementa {@link Serializable} para garantizar la serialización correcta de la clave.</p>
 *
 * @author DriveZone
 */
@Embeddable
@EqualsAndHashCode
@Getter
@Setter
@NoArgsConstructor
@ToString
public class AccesorioHasOrdenCompraId implements Serializable {
    /**
     * Identificador del accesorio.
     */
    @Column(name = "id_accesorio")
    private int idAccesorio;
    /**
     * Identificador de la orden de compra.
     */
    @Column(name = "id_ordencompra")
    private int idOrdenCompra;

}
