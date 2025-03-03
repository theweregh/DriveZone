package com.DriveZone.DriveZone.dao;

import com.DriveZone.DriveZone.models.Usuario;
import de.mkammerer.argon2.Argon2;
import de.mkammerer.argon2.Argon2Factory;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.springframework.stereotype.Repository;

import java.util.List;
import org.springframework.transaction.annotation.Transactional;

@Repository
@Transactional
public class UsuarioDaoImp  implements UsuarioDao{
    @PersistenceContext
    EntityManager entityManager;

    @Override
    public List<Usuario> getUsers() {
        String query = "FROM Usuario";
        List<Usuario> resultList = entityManager.createQuery(query).getResultList();
        return resultList;
    }

    @Override
    public void deleteUser(String id) {
        Usuario user = entityManager.find(Usuario.class, id);
        entityManager.remove(user);
    }

    @Override
    public void registrar(Usuario usuario) {
        entityManager.merge(usuario);
    }
    @Override
    public Usuario obtenerUsuarioPorCredenciales(Usuario usuario) {
        String query = "FROM Usuario WHERE correo = :correo";
        List<Usuario> lista = entityManager.createQuery(query).setParameter("correo", usuario.getCorreo()).getResultList();
        if(lista.isEmpty()) {
            return null;
        }
        String passwordHashed = lista.get(0).getPassword();
        Argon2 argon2 = Argon2Factory.create(Argon2Factory.Argon2Types.ARGON2id);
        if(argon2.verify(passwordHashed,usuario.getPassword())) {
            return lista.get(0);
        }
        return null;
    }

    @Override
    public void actualizarUsuario(Usuario usuario) {
        entityManager.merge(usuario);
    }
    @Override
    public Usuario getUserById(int id) {
        return entityManager.find(Usuario.class, id);
    }
}
