package com.DriveZone.DriveZone.dao;

import com.DriveZone.DriveZone.models.Usuario;
import de.mkammerer.argon2.Argon2;
import de.mkammerer.argon2.Argon2Factory;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Implementación de la interfaz {@link UsuarioDao} para la gestión de usuarios en la base de datos.
 */
@Repository
@Transactional
public class UsuarioDaoImp implements UsuarioDao {
    @PersistenceContext
    EntityManager entityManager;

    /**
     * Obtiene la lista de todos los usuarios almacenados en la base de datos.
     *
     * @return Lista de usuarios.
     */
    @Override
    public List<Usuario> getUsers() {
        String query = "FROM Usuario";
        List<Usuario> resultList = entityManager.createQuery(query).getResultList();
        return resultList;
    }

    /**
     * Elimina un usuario de la base de datos según su ID.
     *
     * @param id Identificador único del usuario a eliminar.
     */
    @Override
    public void deleteUser(String id) {
        Usuario user = entityManager.find(Usuario.class, id);
        entityManager.remove(user);
    }

    /**
     * Registra un nuevo usuario en la base de datos.
     *
     * @param usuario Objeto de tipo {@link Usuario} a registrar.
     */
    @Override
    public void registrar(Usuario usuario) {
        entityManager.merge(usuario);
    }

    /**
     * Obtiene un usuario a partir de sus credenciales (correo y contraseña).
     * Utiliza el algoritmo Argon2 para la verificación de la contraseña.
     *
     * @param usuario Objeto de tipo {@link Usuario} con las credenciales de autenticación.
     * @return El usuario autenticado si las credenciales son correctas, de lo contrario, {@code null}.
     */
    @Override
    public Usuario obtenerUsuarioPorCredenciales(Usuario usuario) {
        String query = "FROM Usuario WHERE correo = :correo";
        List<Usuario> lista = entityManager.createQuery(query).setParameter("correo", usuario.getCorreo()).getResultList();
        if (lista.isEmpty()) {
            return null;
        }
        String passwordHashed = lista.get(0).getPassword();
        Argon2 argon2 = Argon2Factory.create(Argon2Factory.Argon2Types.ARGON2id);
        if (argon2.verify(passwordHashed, usuario.getPassword())) {
            return lista.get(0);
        }
        return null;
    }

    /**
     * Actualiza la información de un usuario en la base de datos.
     *
     * @param usuario Objeto de tipo {@link Usuario} con la información actualizada.
     */
    @Override
    public void actualizarUsuario(Usuario usuario) {
        entityManager.merge(usuario);
    }

    /**
     * Busca un usuario en la base de datos por su ID.
     *
     * @param id Identificador único del usuario.
     * @return El usuario encontrado o {@code null} si no existe.
     */
    @Override
    public Usuario getUserById(int id) {
        return entityManager.find(Usuario.class, id);
    }

    /**
     * Obtiene un usuario por su dirección de correo electrónico.
     *
     * @param correo Correo electrónico del usuario a buscar.
     * @return El usuario correspondiente al correo proporcionado o {@code null} si no existe.
     */
    @Override
    public Usuario obtenerUsuarioPorCorreo(String correo) {
        String query = "FROM Usuario WHERE correo = :correo";
        List<Usuario> lista = entityManager.createQuery(query).setParameter("correo", correo).getResultList();
        return lista.isEmpty() ? null : lista.get(0);
    }
}
