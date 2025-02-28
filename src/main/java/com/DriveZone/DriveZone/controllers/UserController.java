package com.DriveZone.DriveZone.controllers;

import com.DriveZone.DriveZone.dao.UsuarioDao;
import com.DriveZone.DriveZone.models.Usuario;
import de.mkammerer.argon2.Argon2;
import de.mkammerer.argon2.Argon2Factory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
public class UserController {

    @Autowired
    private UsuarioDao usuarioDao;

    @RequestMapping(value = "api/usuarios/{id}")
    public Usuario getUser(@PathVariable int id) {
        Usuario user = new Usuario(id, "user", "nombre", "cedula", "correo", "direccion", "telefono", "password");
        return user;
    }

    @RequestMapping(value = "api/usuarios", method = RequestMethod.GET)
    public List<Usuario> getUsers() {
        return usuarioDao.getUsers();
    }

    @RequestMapping(value = "api/usuarios", method = RequestMethod.POST)
    public void registerUser(@RequestBody Usuario usuario) {

        Argon2 argon2 = Argon2Factory.create(Argon2Factory.Argon2Types.ARGON2id);
        String hash = argon2.hash(1, 1024, 1, usuario.getPassword());
        usuario.setPassword(hash);
        usuarioDao.registrar(usuario);
    }

    @RequestMapping(value = "api/usuarios/{id}", method = RequestMethod.DELETE)
    public void deleteUser(@PathVariable String id) {
        usuarioDao.deleteUser(id);

    }
    @RequestMapping(value = "usuario3")
    public Usuario findUser() {
        Usuario user = new Usuario(1,"user","nombre","cedula","correo","direccion","telefono","password");
        return user;
    }
}
