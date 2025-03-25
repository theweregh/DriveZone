package com.DriveZone.DriveZone.repository;

import com.DriveZone.DriveZone.models.Cliente;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Repositorio para la gestión de la entidad {@link Cliente}.
 * Extiende {@link JpaRepository} para proporcionar operaciones CRUD sobre la base de datos.
 */
@Repository
public interface ClienteRepository extends JpaRepository<Cliente, Integer> {
    /**
     * Busca clientes cuyo nombre, apellido o cédula coincidan parcial o totalmente con el valor proporcionado.
     *
     * @param nombre   Nombre del cliente o parte del mismo (ignorando mayúsculas/minúsculas).
     * @param apellido Apellido del cliente o parte del mismo (ignorando mayúsculas/minúsculas).
     * @param cedula   Número de cédula exacto del cliente.
     * @return Una lista de clientes que coincidan con los criterios de búsqueda.
     */
    List<Cliente> findByNombreContainingIgnoreCaseOrApellidoContainingIgnoreCaseOrCedula(String nombre, String apellido, String cedula);

    /**
     * Busca clientes por nombre exacto.
     *
     * @param nombre Nombre del cliente.
     * @return Una lista de clientes con el nombre especificado.
     */
    List<Cliente> findByNombre(String nombre);

    /**
     * Busca clientes por cédula exacta.
     *
     * @param cedula Número de cédula del cliente.
     * @return Una lista de clientes con la cédula especificada.
     */
    List<Cliente> findByCedula(String cedula);

}
