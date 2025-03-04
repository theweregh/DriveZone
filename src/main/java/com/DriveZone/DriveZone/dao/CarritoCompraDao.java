package com.DriveZone.DriveZone.dao;

import com.DriveZone.DriveZone.models.CarritoCompra;
import com.DriveZone.DriveZone.models.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
@Repository
public interface CarritoCompraDao extends JpaRepository<CarritoCompra, Integer> {
    // 🔹 Obtener el carrito de un usuario
    List<CarritoCompra> findByUsuario(Usuario usuario);

    // 🔹 Buscar un accesorio en el carrito de un usuario
    Optional<CarritoCompra> findByUsuarioAndAccesorio_Id(Usuario usuario, Integer accesorioId);
    // 🔹 Métodos que faltaban según el controlador:

    // Buscar el carrito por el ID del usuario
    List<CarritoCompra> findByUsuarioId(int usuarioId);

    // Buscar un accesorio en el carrito de un usuario por IDs
    Optional<CarritoCompra> findByUsuarioIdAndAccesorioId(int usuarioId, int accesorioId);
}
