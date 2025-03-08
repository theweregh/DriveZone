package com.DriveZone.DriveZone.repository;

import com.DriveZone.DriveZone.models.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * Repositorio para la gestión de la entidad {@link Usuario}.
 * Extiende {@link JpaRepository} para proporcionar operaciones CRUD sobre la base de datos.
 */
@Repository
public interface UsuarioRepository extends JpaRepository<Usuario, Integer> {
    /**
     * Busca un usuario por su correo electrónico.
     *
     * @param correo Correo electrónico del usuario.
     * @return Un {@link Optional} que contiene el usuario si se encuentra, o vacío si no existe.
     */
    Optional<Usuario> findByCorreo(String correo);

    /**
     * Verifica si existe un usuario con el nombre de usuario especificado.
     *
     * @param username Nombre de usuario a verificar.
     * @return {@code true} si el usuario existe, {@code false} en caso contrario.
     */
    boolean existsByUsername(String username);

    /**
     * Verifica si existe un usuario con el correo electrónico especificado.
     *
     * @param correo Correo electrónico a verificar.
     * @return {@code true} si el correo ya está registrado, {@code false} en caso contrario.
     */
    boolean existsByCorreo(String correo);
}
