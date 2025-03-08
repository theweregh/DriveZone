package com.DriveZone.DriveZone.models;

/**
 * Enumeración que define los roles de usuario en el sistema.
 */
public enum RolUsuario {
    /**
     * Representa a un usuario con permisos administrativos.
     */
    ADMINISTRATIVO,
    /**
     * Representa a un empleado dentro de la empresa.
     */
    EMPLEADO,
    /**
     * Representa a un asesor de ventas que gestiona clientes y productos.
     */
    ASESOR_VENTAS,
    /**
     * Representa a un cliente que realiza compras en el sistema.
     */
    CLIENTE
}
