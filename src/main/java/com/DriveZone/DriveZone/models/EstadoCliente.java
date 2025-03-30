package com.DriveZone.DriveZone.models;
/**
 * Representa los posibles estados de un cliente en el sistema.
 *
 * <p>Estos estados pueden utilizarse para determinar la disponibilidad o actividad de un cliente.</p>
 */
public enum EstadoCliente {
    /**
     * Cliente activo en el sistema, puede realizar compras y operaciones.
     */
    activo,
    /**
     * Cliente inactivo, no puede realizar compras pero su información sigue almacenada.
     */
    inactivo,
    /**
     * Cliente fuera de servicio, puede indicar un bloqueo o suspensión definitiva.
     */
    fueraServicio
}
