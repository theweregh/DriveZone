package com.DriveZone.DriveZone.controllers;

import com.DriveZone.DriveZone.dao.AccesorioDao;
import com.DriveZone.DriveZone.dao.CarritoCompraDao;
import com.DriveZone.DriveZone.dao.UsuarioDao;
import com.DriveZone.DriveZone.models.Accesorio;
import com.DriveZone.DriveZone.models.CarritoCompra;
import com.DriveZone.DriveZone.models.Usuario;
import com.DriveZone.DriveZone.utils.JWTUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

/**
 * Controlador REST para gestionar el carrito de compras de los usuarios.
 * Proporciona endpoints para agregar, actualizar, eliminar y procesar compras,
 * con autenticación mediante JWT.
 */
@RestController
@RequestMapping("/api/carrito")
public class CarritoController {
    private static final Logger logger = LoggerFactory.getLogger(CarritoController.class);
    private final CarritoCompraDao carritoCompraDao;
    @Autowired
    private UsuarioDao usuarioDao;
    @Autowired
    private AccesorioDao accesorioDao;
    @Autowired
    private JWTUtil jwtUtil;

    /**
     * Constructor del controlador.
     *
     * @param carritoCompraDao DAO para manejar el carrito de compras.
     */
    public CarritoController(CarritoCompraDao carritoCompraDao) {
        this.carritoCompraDao = carritoCompraDao;
    }

    /**
     * Obtiene el carrito de compras de un usuario específico.
     *
     * @param usuarioId ID del usuario.
     * @return Lista de productos en el carrito.
     */
    @GetMapping("/{usuarioId}")
    public ResponseEntity<List<CarritoCompra>> obtenerCarrito(@PathVariable int usuarioId) {
        List<CarritoCompra> carrito = carritoCompraDao.findByUsuario(usuarioDao.getUserById(usuarioId));
        return ResponseEntity.ok(carrito);
    }

    /**
     * Agrega un producto al carrito de compras de un usuario.
     *
     * @param token         Token de autenticación JWT.
     * @param nuevoProducto Producto a agregar.
     * @return Respuesta HTTP con mensaje de estado.
     */
    @PostMapping("/agregar")
    public ResponseEntity<String> agregarAlCarrito(
            @RequestHeader(value = "Authorization") String token,
            @RequestBody CarritoCompra nuevoProducto) {

        logger.info("Solicitud para agregar producto al carrito recibida. Token: {}", token);

        // Obtener ID de usuario desde el token
        String userId = jwtUtil.getKey(token);
        logger.info("Usuario ID extraído del token: {}", userId);

        // Validar el token antes de continuar
        if (!validarToken(token)) {
            logger.warn("Intento de acceso con token inválido.");
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Token inválido");
        }

        int usuarioId;
        try {
            usuarioId = Integer.parseInt(userId);
        } catch (NumberFormatException e) {
            logger.error("Error al convertir el ID del usuario: {}", userId, e);
            return ResponseEntity.badRequest().body("Error: Usuario ID no válido");
        }

        logger.info("Buscando accesorio con ID: {}", nuevoProducto.getAccesorio().getId());
        Optional<Accesorio> accesorioOpt = accesorioDao.findById(nuevoProducto.getAccesorio().getId());
        if (!accesorioOpt.isPresent()) {
            logger.warn("Accesorio con ID {} no encontrado.", nuevoProducto.getAccesorio().getId());
            return ResponseEntity.badRequest().body("❌ Error: Accesorio no encontrado.");
        }

        Accesorio accesorio = accesorioOpt.get();
        int stockDisponible = accesorio.getStock();
        logger.info("Stock disponible del accesorio {}: {}", accesorio.getId(), stockDisponible);

        Optional<CarritoCompra> existente = carritoCompraDao.findByUsuarioIdAndAccesorioId(usuarioId, nuevoProducto.getAccesorio().getId());
        int cantidadEnCarrito = existente.map(CarritoCompra::getCantidad).orElse(0);

        if (cantidadEnCarrito + nuevoProducto.getCantidad() > stockDisponible) {
            logger.warn("Stock insuficiente para el accesorio {}. Stock: {}, Cantidad solicitada: {}",
                    accesorio.getId(), stockDisponible, nuevoProducto.getCantidad());
            return ResponseEntity.badRequest().body("❌ No hay suficiente stock disponible.");
        }

        if (existente.isPresent()) {
            CarritoCompra item = existente.get();
            item.setCantidad(item.getCantidad() + nuevoProducto.getCantidad());
            carritoCompraDao.save(item);
            logger.info("Cantidad actualizada en el carrito para el accesorio {}.", accesorio.getId());
            return ResponseEntity.ok("✅ Cantidad actualizada en el carrito.");
        } else {
            nuevoProducto.setUsuario(new Usuario(usuarioId));
            carritoCompraDao.save(nuevoProducto);
            logger.info("Producto agregado al carrito. Usuario ID: {}, Accesorio ID: {}", usuarioId, accesorio.getId());
            return ResponseEntity.ok("✅ Producto agregado al carrito.");
        }
    }

    /**
     * Actualiza la cantidad de un producto en el carrito del usuario autenticado.
     *
     * @param token               Token de autorización del usuario. (Requerido)
     * @param productoActualizado Objeto `CarritoCompra` con la nueva cantidad y el accesorio a actualizar.
     * @return ResponseEntity con un mensaje indicando el resultado de la operación.
     * - 200 OK: Si la cantidad fue actualizada correctamente.
     * - 400 Bad Request: Si el accesorio es nulo o no está en el carrito.
     * - 401 Unauthorized: Si el token es inválido o no proporcionado.
     */
    @PutMapping("/actualizar")
    public ResponseEntity<String> actualizarCantidadCarrito(
            @RequestHeader(value = "Authorization", required = false) String token,
            @RequestBody CarritoCompra productoActualizado) {

        System.out.println("🔹 Token recibido: " + token);
        System.out.println("🔹 Producto recibido: " + productoActualizado);

        if (token == null || token.isEmpty()) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Token no proporcionado");
        }

        String userId = jwtUtil.getKey(token);
        System.out.println("🔹 Usuario ID extraído: " + userId);

        if (!validarToken(token)) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Token inválido");
        }

        if (productoActualizado.getAccesorio() == null /*|| productoActualizado.getAccesorio().getId() == null*/) {
            return ResponseEntity.badRequest().body("Error: Accesorio nulo o sin ID");
        }

        // 🔹 Buscar el producto en el carrito por ID del accesorio y usuario
        Optional<CarritoCompra> carritoItem = carritoCompraDao.findByUsuarioAndAccesorio_Id(usuarioDao.getUserById(Integer.parseInt(userId)), productoActualizado.getAccesorio().getId());

        if (!carritoItem.isPresent()) {
            return ResponseEntity.badRequest().body("El producto no está en el carrito");
        }

        // 🔹 Actualizar la cantidad y guardar
        CarritoCompra item = carritoItem.get();
        item.setCantidad(productoActualizado.getCantidad());
        carritoCompraDao.save(item);  // 🔹 Guardar en la BD

        return ResponseEntity.ok("Cantidad actualizada correctamente");
    }

    /**
     * Elimina un producto del carrito.
     *
     * @param token       Token de autenticación JWT.
     * @param accesorioId ID del accesorio a eliminar.
     * @return Respuesta HTTP con mensaje de estado.
     */
    @DeleteMapping("/eliminar/{accesorioId}")
    public ResponseEntity<String> eliminarDelCarrito(
            @RequestHeader(value = "Authorization") String token,
            @PathVariable int accesorioId) {

        logger.info("Solicitud para eliminar producto del carrito. Token: {}, Accesorio ID: {}", token, accesorioId);

        // Obtener ID de usuario desde el token
        String userId = jwtUtil.getKey(token);
        logger.info("Usuario ID extraído: {}", userId);

        if (!validarToken(token)) {
            logger.warn("Intento de eliminación con token inválido.");
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Token inválido");
        }

        int usuarioId;
        try {
            usuarioId = Integer.parseInt(userId);
        } catch (NumberFormatException e) {
            logger.error("Error al convertir el ID del usuario: {}", userId, e);
            return ResponseEntity.badRequest().body("Error: Usuario ID no válido");
        }

        List<CarritoCompra> carrito = carritoCompraDao.findByUsuarioId(usuarioId);
        Optional<CarritoCompra> existente = carrito.stream()
                .filter(item -> item.getAccesorio() != null && item.getAccesorio().getId() == accesorioId)
                .findFirst();

        if (existente.isPresent()) {
            carritoCompraDao.delete(existente.get());
            logger.info("Producto eliminado del carrito. Usuario ID: {}, Accesorio ID: {}", usuarioId, accesorioId);
            return ResponseEntity.ok("Producto eliminado del carrito");
        } else {
            logger.warn("Intento de eliminación de un producto no existente en el carrito. Usuario ID: {}, Accesorio ID: {}", usuarioId, accesorioId);
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Producto no encontrado en el carrito");
        }
    }

    /**
     * Vacía el carrito de compras de un usuario.
     *
     * @param usuarioId ID del usuario.
     * @return Respuesta HTTP con mensaje de estado.
     */
    @PostMapping("/procesar/{usuarioId}")
    public ResponseEntity<String> procesarCompra(@PathVariable int usuarioId) {
        List<CarritoCompra> carrito = carritoCompraDao.findByUsuario(usuarioDao.getUserById(usuarioId));
        carritoCompraDao.deleteAll(carrito);
        return ResponseEntity.ok("Compra procesada con éxito");
    }

    /**
     * Valida si un token JWT es válido.
     *
     * @param token Token de autenticación.
     * @return true si es válido, false si no.
     */
    private boolean validarToken(String token) {
        String userId = jwtUtil.getKey(token);
        //se puede verificar si este user esta en la db
        return userId != null;
    }

    /**
     * Obtiene el carrito de compras del usuario autenticado.
     *
     * @param token Token de autorización del usuario.
     * @return ResponseEntity con la lista de productos en el carrito del usuario.
     * - 200 OK: Devuelve la lista de productos en el carrito.
     * - 204 No Content: Si el carrito está vacío.
     * - 400 Bad Request: Si el ID del usuario extraído del token no es válido.
     * - 401 Unauthorized: Si el token es inválido.
     */
    @GetMapping("/obtener")
    public ResponseEntity<?> obtenerCarrito(@RequestHeader(value = "Authorization") String token) {

        System.out.println("Token recibido: " + token);

        // Obtener ID de usuario desde el token
        String userId = jwtUtil.getKey(token);
        System.out.println("Usuario ID extraído: " + userId);

        // Validar el token antes de continuar
        if (!validarToken(token)) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Token inválido");
        }

        int usuarioId;
        try {
            usuarioId = Integer.parseInt(userId);
        } catch (NumberFormatException e) {
            return ResponseEntity.badRequest().body("Error: Usuario ID no válido");
        }

        // Obtener los productos en el carrito del usuario
        List<CarritoCompra> carrito = carritoCompraDao.findByUsuarioId(usuarioId);

        if (carrito.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NO_CONTENT).body("El carrito está vacío");
        }

        return ResponseEntity.ok(carrito);
    }

    /**
     * Obtiene la cantidad de productos en el carrito del usuario autenticado.
     *
     * @param token Token de autorización del usuario.
     * @return ResponseEntity con la lista de productos en el carrito del usuario.
     * - 200 OK: Devuelve la lista de productos en el carrito.
     * - 400 Bad Request: Si el ID del usuario extraído del token no es válido.
     * - 401 Unauthorized: Si el token es inválido.
     */
    @GetMapping("/carrito")
    public ResponseEntity<List<CarritoCompra>> obtenerCantidadCarrito(@RequestHeader(value = "Authorization") String token) {
        // Validar el token
        if (!validarToken(token)) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        // Obtener ID de usuario
        String userId = jwtUtil.getKey(token);
        int usuarioId;
        try {
            usuarioId = Integer.parseInt(userId);
        } catch (NumberFormatException e) {
            return ResponseEntity.badRequest().build();
        }

        // Buscar los productos en el carrito del usuario
        List<CarritoCompra> carrito = carritoCompraDao.findByUsuarioId(usuarioId);
        return ResponseEntity.ok(carrito);
    }
}
