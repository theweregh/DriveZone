package com.DriveZone.DriveZone.dao;

import com.DriveZone.DriveZone.models.Accesorio;
import com.DriveZone.DriveZone.models.Usuario;
import de.mkammerer.argon2.Argon2;
import de.mkammerer.argon2.Argon2Factory;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Implementación del DAO para la gestión de accesorios en la base de datos.
 */
@Repository
@Transactional
public class AccesorioDaoImp {

    @PersistenceContext
    EntityManager entityManager;

    /**
     * Obtiene la lista de todos los accesorios almacenados en la base de datos.
     *
     * @return Lista de accesorios.
     */
    public List<Accesorio> getAccesorios() {
        String query = "FROM Accesorio";
        List<Accesorio> resultList = entityManager.createQuery(query).getResultList();
        return resultList;
    }

    /**
     * Elimina un accesorio de la base de datos mediante su identificador.
     *
     * @param id Identificador del accesorio a eliminar.
     */
    public void deleteAccesorio(String id) {
        Accesorio accesorio = entityManager.find(Accesorio.class, id);
        entityManager.remove(accesorio);
    }

    /**
     * Registra un nuevo accesorio en la base de datos o actualiza uno existente.
     *
     * @param accesorio Objeto accesorio a registrar o actualizar.
     */
    public void registrar(Accesorio accesorio) {
        entityManager.merge(accesorio);
    }

    /**
     * Verifica las credenciales de un usuario basado en su correo y contraseña.
     *
     * @param usuario Objeto usuario con las credenciales a verificar.
     * @return Usuario autenticado si las credenciales son correctas, null en caso contrario.
     */
    public Usuario obtenerAccesorioPorCredenciales(Usuario usuario) {
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
}
