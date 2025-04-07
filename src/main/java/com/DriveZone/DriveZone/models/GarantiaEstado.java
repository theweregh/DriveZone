package com.DriveZone.DriveZone.models;

public enum GarantiaEstado {
    pendiente,
    garantia,
    devolucion,
    rechazada;
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
