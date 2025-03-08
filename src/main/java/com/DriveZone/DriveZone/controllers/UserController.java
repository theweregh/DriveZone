package com.DriveZone.DriveZone.controllers;

import com.DriveZone.DriveZone.dao.CarritoCompraDao;
import com.DriveZone.DriveZone.dao.UsuarioDao;
import com.DriveZone.DriveZone.models.CarritoCompra;
import com.DriveZone.DriveZone.models.LogBusqueda;
import com.DriveZone.DriveZone.models.Usuario;
import com.DriveZone.DriveZone.repository.CarritoRepository;
import com.DriveZone.DriveZone.repository.UsuarioRepository;
import com.DriveZone.DriveZone.utils.JWTUtil;
import de.mkammerer.argon2.Argon2;
import de.mkammerer.argon2.Argon2Factory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Controlador REST para la gestión de usuarios en el sistema.
 */
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

    /**
     * Obtiene la lista de todos los usuarios registrados en el sistema.
     *
     * @param token Token de autorización.
     * @return Lista de usuarios si el token es válido, de lo contrario retorna null.
     */
    @RequestMapping(value = "api/usuarios", method = RequestMethod.GET)
    public List<Usuario> getUsers(@RequestHeader(value = "Authorization") String token) {
        String userId = jwtUtil.getKey(token);
        if (!validarToken(token)) {
            return null;
        }
        return usuarioDao.getUsers();
    }

    /**
     * Registra un nuevo usuario en el sistema, verificando que el username y correo no estén en uso.
     *
     * @param usuario Objeto {@link Usuario} con la información del nuevo usuario.
     * @return {@link ResponseEntity} con mensaje de éxito o error según corresponda.
     */
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

    /**
     * Elimina un usuario del sistema por su ID.
     *
     * @param token Token de autorización.
     * @param id    ID del usuario a eliminar.
     */
    @RequestMapping(value = "api/usuarios/{id}", method = RequestMethod.DELETE)
    public void deleteUser(@RequestHeader(value = "Authorization") String token, @PathVariable String id) {
        if (!validarToken(token)) {
            return;
        }
        usuarioDao.deleteUser(id);

    }

    /**
     * Actualiza el estado de un usuario en el sistema.
     *
     * @param token   Token de autorización.
     * @param id      ID del usuario a actualizar.
     * @param usuario Objeto {@link Usuario} con el nuevo estado.
     */
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

    /**
     * Valida si un token es válido o no.
     *
     * @param token Token de autenticación.
     * @return true si el token es válido, false en caso contrario.
     */
    private boolean validarToken(String token) {
        String userId = jwtUtil.getKey(token);
        return userId != null;
    }

    /**
     * Obtiene un usuario por su correo electrónico.
     *
     * @param token  Token de autorización.
     * @param correo Correo del usuario a buscar.
     * @return Usuario encontrado o null si no es autorizado.
     */
    @RequestMapping(value = "api/usuario/{correo}", method = RequestMethod.GET)
    public Usuario obtenerUsuario(@RequestHeader(value = "Authorization") String token, @PathVariable String correo) {
        if (!validarToken(token)) {
            return null;
        }

        // Obtener el usuario autenticado
        String userId = jwtUtil.getKey(token);
        Usuario usuarioAutenticado = usuarioDao.getUserById(Integer.parseInt(userId));

        // Registrar la búsqueda en el log
        LogBusqueda.registrarBusqueda(usuarioAutenticado.getUsername(), correo);

        return usuarioDao.obtenerUsuarioPorCorreo(correo);
    }

    /**
     * Registra una búsqueda realizada por un usuario en los logs del sistema.
     *
     * @param token            Token de autorización.
     * @param criterioBusqueda Criterio de búsqueda ingresado por el usuario.
     * @return {@link ResponseEntity} con mensaje de éxito o error según corresponda.
     */
    @RequestMapping(value = "api/logs/busqueda", method = RequestMethod.POST)
    public ResponseEntity<String> registrarBusqueda(@RequestHeader(value = "Authorization") String token,
                                                    @RequestBody String criterioBusqueda) {
        if (!validarToken(token)) {
            return ResponseEntity.status(401).body("No autorizado");
        }

        // Obtener el usuario autenticado
        String userId = jwtUtil.getKey(token);
        Usuario usuarioAutenticado = usuarioDao.getUserById(Integer.parseInt(userId));

        // Guardar la búsqueda en el log
        LogBusqueda.registrarBusqueda(usuarioAutenticado.getUsername(), criterioBusqueda);

        return ResponseEntity.ok("Búsqueda registrada correctamente.");
    }

    /**
     * Actualiza la información de un usuario en el sistema.
     *
     * @param token              Token de autorización.
     * @param id                 ID del usuario a actualizar.
     * @param usuarioActualizado Objeto {@link Usuario} con los datos actualizados.
     * @return {@link ResponseEntity} con mensaje de éxito o error según corresponda.
     */
    @RequestMapping(value = "api/usuarios/{id}", method = RequestMethod.PUT)
    public ResponseEntity<String> actualizarUsuario(@RequestHeader(value = "Authorization") String token,
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
