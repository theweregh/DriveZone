package com.DriveZone.DriveZone.models;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

/**
 * Representa un registro en el historial de accesorios.
 *
 * <p>Esta entidad almacena información sobre las acciones realizadas
 * en los accesorios, incluyendo si fueron agregados o eliminados,
 * el nombre del accesorio, su ID y la fecha en la que se realizó la acción.</p>
 */
@Entity
@Table(name = "historialAccesorio")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class HistorialAccesorio {
    /**
     * Identificador único del historial de accesorios.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int idHistorialAccesorio;
    /**
     * Acción realizada sobre el accesorio (AGREGADO o ELIMINADO).
     */
    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private Accion accion;
    /**
     * Nombre del accesorio sobre el cual se realizó la acción.
     */
    @Column(nullable = false)
    private String nombreAccesorio;
    /**
     * Identificador del accesorio asociado a la acción.
     */
    @Column(nullable = false)
    private int idAccesorio;
    /**
     * Fecha y hora en la que se realizó la acción sobre el accesorio.
     */
    @Column(nullable = false)
    private LocalDateTime fecha;

    /**
     * Constructor que inicializa un historial con la acción realizada,
     * el nombre del accesorio y su ID.
     *
     * @param accion          Acción realizada (AGREGADO o ELIMINADO).
     * @param nombreAccesorio Nombre del accesorio afectado.
     * @param idAccesorio     Identificador del accesorio.
     */
    public HistorialAccesorio(Accion accion, String nombreAccesorio, int idAccesorio) {
        this.accion = accion;
        this.nombreAccesorio = nombreAccesorio;
        this.idAccesorio = idAccesorio;
        this.fecha = LocalDateTime.now();
    }
}
