package com.DriveZone.DriveZone.repository;

import com.DriveZone.DriveZone.models.CarritoCompra;
import com.DriveZone.DriveZone.models.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * Repositorio para la gestión de la entidad {@link CarritoCompra}.
 * Extiende {@link JpaRepository} para proporcionar operaciones CRUD sobre la base de datos.
 */
@Repository
public interface CarritoRepository extends JpaRepository<CarritoCompra, Integer> {
    /**
     * Busca el carrito de compras de un usuario específico.
     *
     * @param usuario Objeto de tipo {@link Usuario} del que se desea obtener el carrito de compras.
     * @return Un {@link Optional} que contiene el carrito de compras si se encuentra, o vacío si no existe.
     */
    Optional<CarritoCompra> findByUsuario(Usuario usuario);
}
