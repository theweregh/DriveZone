package com.DriveZone.DriveZone.controllers;

import com.DriveZone.DriveZone.dao.UsuarioDao;
import com.DriveZone.DriveZone.models.Usuario;
import com.DriveZone.DriveZone.utils.JWTUtil;
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
    @Autowired
    private JWTUtil jwtUtil;

    @RequestMapping(value = "api/usuarios/{id}")
    public Usuario getUser(@PathVariable int id) {
        Usuario user = new Usuario(id, "user", "nombre", "cedula", "correo", "direccion", "telefono", "password","activo","rol");
        return user;
    }

    @RequestMapping(value = "api/usuarios", method = RequestMethod.GET)
    public List<Usuario> getUsers(@RequestHeader(value="Authorization")String token) {
        String userId = jwtUtil.getKey(token);
        if(!validarToken(token)){
            return null;
        }
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
    public void deleteUser(@RequestHeader(value="Authorization")String token,@PathVariable String id) {
         if(!validarToken(token)){
            return;
        }
        usuarioDao.deleteUser(id);

    }

    @RequestMapping(value = "api/usuarios/{id}/estado", method = RequestMethod.PUT)
    public void updateEstado(@RequestHeader(value = "Authorization") String token,
                             @PathVariable int id,
                             @RequestBody Usuario usuario) {
        if (!validarToken(token)) {
            return;
        }

        Usuario userExistente = usuarioDao.getUserById(id);
        if (userExistente != null) {
            userExistente.setEstado(usuario.getEstado());
            usuarioDao.actualizarUsuario(userExistente);
        }
    }

    private boolean validarToken(String token) {
        String userId = jwtUtil.getKey(token);
        return userId != null;
    }

}
