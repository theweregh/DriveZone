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

@Repository
@Transactional
public class AccesorioDaoImp {

    @PersistenceContext
    EntityManager entityManager;

    public List<Accesorio> getAccesorios() {
        String query = "FROM Accesorio";
        List<Accesorio> resultList = entityManager.createQuery(query).getResultList();
        return resultList;
    }

    public void deleteAccesorio(String id) {
        Accesorio accesorio = entityManager.find(Accesorio.class, id);
        entityManager.remove(accesorio);
    }

    public void registrar(Accesorio accesorio) {
        entityManager.merge(accesorio);
    }

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
