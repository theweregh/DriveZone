package com.DriveZone.DriveZone.dao;

import com.DriveZone.DriveZone.models.Usuario;
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
    public boolean verificarCredenciales(Usuario usuario) {
        String query = "FROM Usuario WHERE correo = :correo AND password = :password";
        List<Usuario> lista = entityManager.createQuery(query).setParameter("correo", usuario.getCorreo()).setParameter("password", usuario.getPassword()).getResultList();
        return !lista.isEmpty();
    }
}
