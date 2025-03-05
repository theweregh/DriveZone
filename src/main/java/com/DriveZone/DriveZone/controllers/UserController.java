package com.DriveZone.DriveZone.controllers;

import com.DriveZone.DriveZone.dao.CarritoCompraDao;
import com.DriveZone.DriveZone.dao.OrdenCompraDao;
import com.DriveZone.DriveZone.dao.UsuarioDao;
import com.DriveZone.DriveZone.models.*;
import com.DriveZone.DriveZone.repository.CarritoRepository;
import com.DriveZone.DriveZone.repository.UsuarioRepository;
import com.DriveZone.DriveZone.utils.JWTUtil;
import de.mkammerer.argon2.Argon2;
import de.mkammerer.argon2.Argon2Factory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
public class UserController {

    @Autowired
    private UsuarioDao usuarioDao;
    @Autowired
    private CarritoCompraDao carritoCompraDao;
    @Autowired
    private UsuarioRepository usuarioRepository;
    @Autowired
    private CarritoRepository carritoRepository;
    @Autowired
    private JWTUtil jwtUtil;
    @Autowired
    private LogBusqueda logBusquedaService;
    /*
    @RequestMapping(value = "api/usuarios/{id}")
    public Usuario getUser(@PathVariable int id) {
        Usuario user = new Usuario(id, "user", "nombre", "cedula", "correo", "direccion", "telefono", "password", , RolUsuario.EMPLEADO);
        return user;
    }*/

    @RequestMapping(value = "api/usuarios", method = RequestMethod.GET)
    public List<Usuario> getUsers(@RequestHeader(value="Authorization")String token) {
        String userId = jwtUtil.getKey(token);
        if(!validarToken(token)){
            return null;
        }
        return usuarioDao.getUsers();
    }

    /*@RequestMapping(value = "api/usuarios", method = RequestMethod.POST)
    public void registerUser(@RequestBody Usuario usuario) {

        Argon2 argon2 = Argon2Factory.create(Argon2Factory.Argon2Types.ARGON2id);
        String hash = argon2.hash(1, 1024, 1, usuario.getPassword());
        usuario.setPassword(hash);
        // 🔹 Crear carrito vacío al registrar usuario
        usuarioRepository.save(usuario);
        CarritoCompra carrito = new CarritoCompra();
        //carrito.setAccesorio(new Accesorio());
        carrito.setUsuario(usuario);
        carritoRepository.save(carrito);
        usuarioDao.registrar(usuario);
    }*/
    @RequestMapping(value = "api/usuarios", method = RequestMethod.POST)
public ResponseEntity<String> registerUser(@RequestBody Usuario usuario) {

    // Verificar si el usuario o el correo ya están registrados
    if (usuarioRepository.existsByUsername(usuario.getUsername())) {
        return ResponseEntity.badRequest().body("❌ El nombre de usuario ya está en uso.");
    }

    if (usuarioRepository.existsByCorreo(usuario.getCorreo())) {
        return ResponseEntity.badRequest().body("❌ El correo electrónico ya está registrado.");
    }

    // Hash de la contraseña
    Argon2 argon2 = Argon2Factory.create(Argon2Factory.Argon2Types.ARGON2id);
    String hash = argon2.hash(1, 1024, 1, usuario.getPassword());
    usuario.setPassword(hash);

    // Guardar usuario en la base de datos
    usuarioRepository.save(usuario);

    // Crear carrito vacío al registrar usuario
    CarritoCompra carrito = new CarritoCompra();
    carrito.setUsuario(usuario);
    carritoRepository.save(carrito);

    usuarioDao.registrar(usuario);

    return ResponseEntity.ok("✅ Usuario registrado correctamente.");
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
    /*@RequestMapping(value = "api/usuario/{correo}", method = RequestMethod.GET)
public Usuario obtenerUsuario(@PathVariable String correo) {
    return usuarioDao.obtenerUsuarioPorCorreo(correo);
}*/
    @RequestMapping(value = "api/usuario/{correo}", method = RequestMethod.GET)
public Usuario obtenerUsuario(@RequestHeader(value="Authorization") String token, @PathVariable String correo) {
    if (!validarToken(token)) {
        return null;
    }

    // Obtener el usuario autenticado
    String userId = jwtUtil.getKey(token);
    Usuario usuarioAutenticado = usuarioDao.getUserById(Integer.parseInt(userId));

    // Registrar la búsqueda en el log
    logBusquedaService.registrarBusqueda(usuarioAutenticado.getUsername(), correo);

    return usuarioDao.obtenerUsuarioPorCorreo(correo);
}
@RequestMapping(value = "api/logs/busqueda", method = RequestMethod.POST)
public ResponseEntity<String> registrarBusqueda(@RequestHeader(value="Authorization") String token,
                                                @RequestBody String criterioBusqueda) {
    if (!validarToken(token)) {
        return ResponseEntity.status(401).body("No autorizado");
    }

    // Obtener el usuario autenticado
    String userId = jwtUtil.getKey(token);
    Usuario usuarioAutenticado = usuarioDao.getUserById(Integer.parseInt(userId));

    // Guardar la búsqueda en el log
    logBusquedaService.registrarBusqueda(usuarioAutenticado.getUsername(), criterioBusqueda);

    return ResponseEntity.ok("Búsqueda registrada correctamente.");
}
/*@RequestMapping(value = "api/usuarios/{id}", method = RequestMethod.PUT)
public ResponseEntity<String> actualizarUsuario(@RequestHeader(value="Authorization") String token,
                                                @PathVariable int id,
                                                @RequestBody Usuario usuarioActualizado) {
    if (!validarToken(token)) {
        return ResponseEntity.status(401).body("No autorizado");
    }

    Usuario usuarioExistente = usuarioDao.getUserById(id);
    if (usuarioExistente == null) {
        return ResponseEntity.status(404).body("Usuario no encontrado");
    }

    // Actualizar datos
    usuarioExistente.setUsername(usuarioActualizado.getUsername());
    usuarioExistente.setNombres(usuarioActualizado.getNombres());
    usuarioExistente.setCedula(usuarioActualizado.getCedula());
    usuarioExistente.setCorreo(usuarioActualizado.getCorreo());
    usuarioExistente.setDireccion(usuarioActualizado.getDireccion());
    usuarioExistente.setTelefono(usuarioActualizado.getTelefono());
    usuarioExistente.setRol(usuarioActualizado.getRol());

    usuarioDao.actualizarUsuario(usuarioExistente);

    return ResponseEntity.ok("Usuario actualizado correctamente.");
}*/
    @RequestMapping(value = "api/usuarios/{id}", method = RequestMethod.PUT)
public ResponseEntity<String> actualizarUsuario(@RequestHeader(value="Authorization") String token,
                                                @PathVariable int id,
                                                @RequestBody Usuario usuarioActualizado) {
    if (!validarToken(token)) {
        return ResponseEntity.status(401).body("No autorizado");
    }

    Usuario usuarioExistente = usuarioDao.getUserById(id);
    if (usuarioExistente == null) {
        return ResponseEntity.status(404).body("Usuario no encontrado");
    }

    try {
        // Actualizar datos
        usuarioExistente.setUsername(usuarioActualizado.getUsername());
        usuarioExistente.setNombres(usuarioActualizado.getNombres());
        usuarioExistente.setCedula(usuarioActualizado.getCedula());
        usuarioExistente.setCorreo(usuarioActualizado.getCorreo());
        usuarioExistente.setDireccion(usuarioActualizado.getDireccion());
        usuarioExistente.setTelefono(usuarioActualizado.getTelefono());
        usuarioExistente.setRol(usuarioActualizado.getRol());

        usuarioDao.actualizarUsuario(usuarioExistente);

        return ResponseEntity.ok("Usuario actualizado correctamente.");

    } catch (Exception e) {
        return ResponseEntity.status(500).body("Error interno al actualizar usuario.");
    }
}

}
