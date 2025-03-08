package com.DriveZone.DriveZone.dao;

import com.DriveZone.DriveZone.models.CarritoCompra;
import com.DriveZone.DriveZone.models.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * Repositorio para la entidad {@link CarritoCompra}.
 * Proporciona métodos para interactuar con la base de datos relacionada con los carritos de compra.
 */
@Repository
public interface CarritoCompraDao extends JpaRepository<CarritoCompra, Integer> {
    /**
     * Obtiene una lista de carritos de compra asociados a un usuario específico.
     *
     * @param usuario El usuario del cual se desea obtener el carrito de compra.
     * @return Lista de carritos de compra del usuario.
     */
    List<CarritoCompra> findByUsuario(Usuario usuario);

    /**
     * Busca un accesorio en el carrito de un usuario específico.
     *
     * @param usuario     El usuario propietario del carrito.
     * @param accesorioId El ID del accesorio a buscar en el carrito.
     * @return Un {@link Optional} que contiene el carrito de compra si se encuentra el accesorio.
     */
    Optional<CarritoCompra> findByUsuarioAndAccesorio_Id(Usuario usuario, Integer accesorioId);

    /**
     * Obtiene una lista de carritos de compra asociados a un usuario a partir de su ID.
     *
     * @param usuarioId El ID del usuario.
     * @return Lista de carritos de compra pertenecientes al usuario.
     */
    List<CarritoCompra> findByUsuarioId(int usuarioId);

    /**
     * Busca un accesorio en el carrito de un usuario mediante los IDs del usuario y del accesorio.
     *
     * @param usuarioId   El ID del usuario.
     * @param accesorioId El ID del accesorio a buscar en el carrito.
     * @return Un {@link Optional} con el carrito de compra si se encuentra el accesorio.
     */
    Optional<CarritoCompra> findByUsuarioIdAndAccesorioId(int usuarioId, int accesorioId);
}
