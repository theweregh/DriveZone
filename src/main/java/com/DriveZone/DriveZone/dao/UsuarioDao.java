package com.DriveZone.DriveZone.dao;

import com.DriveZone.DriveZone.models.Usuario;

import java.util.List;

/**
 * Interfaz que define las operaciones de acceso a datos para la entidad {@link Usuario}.
 */
public interface UsuarioDao {
    /**
     * Obtiene la lista de todos los usuarios almacenados en la base de datos.
     *
     * @return Lista de usuarios.
     */
    List<Usuario> getUsers();

    /**
     * Elimina un usuario de la base de datos según su ID.
     *
     * @param id Identificador único del usuario a eliminar.
     */
    void deleteUser(String id);

    /**
     * Registra un nuevo usuario en la base de datos.
     *
     * @param usuario Objeto de tipo {@link Usuario} a registrar.
     */
    void registrar(Usuario usuario);

    /**
     * Obtiene un usuario a partir de sus credenciales (correo y contraseña).
     *
     * @param usuario Objeto de tipo {@link Usuario} con las credenciales de autenticación.
     * @return El usuario autenticado si las credenciales son correctas, de lo contrario, {@code null}.
     */
    Usuario obtenerUsuarioPorCredenciales(Usuario usuario);

    /**
     * Busca un usuario en la base de datos por su ID.
     *
     * @param id Identificador único del usuario.
     * @return El usuario encontrado o {@code null} si no existe.
     */
    Usuario getUserById(int id); // Nuevo método para obtener usuario por ID

    /**
     * Actualiza la información de un usuario en la base de datos.
     *
     * @param usuario Objeto de tipo {@link Usuario} con la información actualizada.
     */
    void actualizarUsuario(Usuario usuario); // Nuevo método para actualizar el usuario

    /**
     * Obtiene un usuario por su dirección de correo electrónico.
     *
     * @param correo Correo electrónico del usuario a buscar.
     * @return El usuario correspondiente al correo proporcionado o {@code null} si no existe.
     */
    Usuario obtenerUsuarioPorCorreo(String correo);
}
