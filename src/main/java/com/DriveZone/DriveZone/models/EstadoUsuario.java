package com.DriveZone.DriveZone.models;

/**
 * Representa los posibles estados de un usuario en el sistema.
 */
public enum EstadoUsuario {
    /**
     * Usuario activo y con acceso al sistema
     */
    ACTIVO,
    /**
     * Usuario inactivo, sin acceso temporal
     */
    INACTIVO,
    /**
     * Usuario fuera de servicio, posiblemente bloqueado o eliminado
     */
    FUERA_SERVICIO
}
