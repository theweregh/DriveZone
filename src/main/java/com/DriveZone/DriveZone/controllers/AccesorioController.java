package com.DriveZone.DriveZone.controllers;

import com.DriveZone.DriveZone.dao.AccesorioDao;
import com.DriveZone.DriveZone.dao.UsuarioDao;
import com.DriveZone.DriveZone.models.Accesorio;
import com.DriveZone.DriveZone.models.Usuario;
import com.DriveZone.DriveZone.utils.JWTUtil;
import de.mkammerer.argon2.Argon2;
import de.mkammerer.argon2.Argon2Factory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class AccesorioController {
    @Autowired
    private AccesorioDao accesorioDao;
    @Autowired
    private JWTUtil jwtUtil;
    @RequestMapping(value = "api/accesorio/{id}")
    public Accesorio getAccesorio(@PathVariable int id) {
        Accesorio accesorio = new Accesorio(10,"steering wheel","color rojo maranello",1,12500D,"xd.png");
        return accesorio;
    }

    @RequestMapping(value = "api/accesorio", method = RequestMethod.GET)
    public List<Accesorio> getAccesorios(@RequestHeader(value="Authorization")String token) {
        System.out.println("Token recibido: " + token);
        String userId = jwtUtil.getKey(token);
        System.out.println("Usuario ID extraído: " + userId);
        if(!validarToken(token)){
            return null;
        }

        return accesorioDao.findAll();
    }
    /*
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

    }*/
    private boolean validarToken(String token){
        String userId = jwtUtil.getKey(token);
        //se puede verificar si este user esta en la db
        return userId != null;
    }
}
