package com.DriveZone.DriveZone.models;

/**
 * Enum que representa los posibles estados de una solicitud de garantía.
 * Cada estado define el proceso o resolución actual de la garantía.
 * Además, proporciona una representación amigable en texto para mostrar al usuario.
 * <p>
 * Los estados posibles son:
 * <ul>
 *     <li><b>pendiente</b>: La solicitud está en espera de ser procesada.</li>
 *     <li><b>garantia</b>: La solicitud fue aceptada y el producto será reemplazado.</li>
 *     <li><b>devolucion</b>: La solicitud fue aceptada y se realizará la devolución del dinero.</li>
 *     <li><b>rechazada</b>: La solicitud fue rechazada por no cumplir las condiciones de garantía.</li>
 * </ul>
 *
 * @author DriveZone Team
 * @version 1.1
 * @since 2025-04-27
 */
public enum GarantiaEstado {
    pendiente,
    garantia,
    devolucion,
    rechazada;

    /**
     * Retorna una representación en texto amigable del estado de la garantía.
     *
     * @return Estado de la garantía en formato legible para el usuario.
     */
    @Override
    public String toString() {
        return switch (this) {
            case pendiente -> "Pendiente";
            case garantia -> "Garantía";
            case devolucion -> "Devolución";
            case rechazada -> "Rechazada";
        };
    }
}
