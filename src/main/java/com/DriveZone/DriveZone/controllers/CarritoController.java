package com.DriveZone.DriveZone.controllers;

import com.DriveZone.DriveZone.dao.AccesorioDao;
import com.DriveZone.DriveZone.dao.CarritoCompraDao;
import com.DriveZone.DriveZone.dao.OrdenCompraDaoImp;
import com.DriveZone.DriveZone.dao.UsuarioDao;
import com.DriveZone.DriveZone.models.*;
import com.DriveZone.DriveZone.utils.JWTUtil;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.text.MessageFormat;
@RestController
@RequestMapping("/api/carrito")
public class CarritoController {
    private final CarritoCompraDao carritoCompraDao;
    @Autowired
    private UsuarioDao usuarioDao;
    @Autowired
    private AccesorioDao accesorioDao;
    @Autowired
    private JWTUtil jwtUtil;
    private static final Logger logger = LoggerFactory.getLogger(CarritoController.class);
    public CarritoController(CarritoCompraDao carritoCompraDao) {
        this.carritoCompraDao = carritoCompraDao;
    }

    // Obtener el carrito del usuario
    @GetMapping("/{usuarioId}")
    public ResponseEntity<List<CarritoCompra>> obtenerCarrito(@PathVariable int usuarioId) {
        List<CarritoCompra> carrito = carritoCompraDao.findByUsuario(usuarioDao.getUserById(usuarioId));
        return ResponseEntity.ok(carrito);
    }

    /*@PostMapping("/agregar")
public ResponseEntity<String> agregarAlCarrito(
        @RequestHeader(value = "Authorization") String token,
        @RequestBody CarritoCompra nuevoProducto) {

    System.out.println("Token recibido: " + token);

    // Obtener ID de usuario desde el token
    String userId = jwtUtil.getKey(token);
    System.out.println("Usuario ID extraído: " + userId);

    // Validar el token antes de continuar
    if (!validarToken(token)) {
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Token inválido");
    }

    // Convertir userId a entero
    int usuarioId;
    try {
        usuarioId = Integer.parseInt(userId);
    } catch (NumberFormatException e) {
        return ResponseEntity.badRequest().body("Error: Usuario ID no válido");
    }

    // Buscar productos en el carrito del usuario
    List<CarritoCompra> carrito = carritoCompraDao.findByUsuarioId(usuarioId);

    // Verificar si el producto ya está en el carrito
    /*Optional<CarritoCompra> existente = carrito.stream()
            .filter(item -> item.getAccesorio().getId()==(nuevoProducto.getAccesorio().getId()))
            .findFirst();*/
    /*Optional<CarritoCompra> existente = carritoCompraDao.findByUsuarioIdAndAccesorioId(usuarioId, nuevoProducto.getAccesorio().getId());
    if (existente.isPresent()) {
        CarritoCompra item = existente.get();
        item.setCantidad(item.getCantidad() + nuevoProducto.getCantidad());
        carritoCompraDao.save(item);
        return ResponseEntity.ok("Cantidad actualizada en el carrito");
    } else {
        // Asignar el usuario al nuevo producto antes de guardarlo
        nuevoProducto.setUsuario(new Usuario(usuarioId)); // Asegúrate de que esta asignación sea válida en tu modelo
        carritoCompraDao.save(nuevoProducto);
        return ResponseEntity.ok("Producto agregado al carrito");
    }
}*/
    /*@PostMapping("/agregar")
public ResponseEntity<String> agregarAlCarrito(
        @RequestHeader(value = "Authorization") String token,
        @RequestBody CarritoCompra nuevoProducto) {

    System.out.println("Token recibido: " + token);

    // Obtener ID de usuario desde el token
    String userId = jwtUtil.getKey(token);
    System.out.println("Usuario ID extraído: " + userId);

    // Validar el token antes de continuar
    if (!validarToken(token)) {
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Token inválido");
    }

    // Convertir userId a entero
    int usuarioId;
    try {
        usuarioId = Integer.parseInt(userId);
    } catch (NumberFormatException e) {
        return ResponseEntity.badRequest().body("Error: Usuario ID no válido");
    }

    // Obtener el accesorio desde la base de datos
    Optional<Accesorio> accesorioOpt = accesorioDao.findById(nuevoProducto.getAccesorio().getId());
    if (!accesorioOpt.isPresent()) {
        return ResponseEntity.badRequest().body("❌ Error: Accesorio no encontrado.");
    }

    Accesorio accesorio = accesorioOpt.get();
    int stockDisponible = accesorio.getStock();

    // Obtener la cantidad actual en el carrito
    Optional<CarritoCompra> existente = carritoCompraDao.findByUsuarioIdAndAccesorioId(usuarioId, nuevoProducto.getAccesorio().getId());
    int cantidadEnCarrito = existente.map(CarritoCompra::getCantidad).orElse(0);

    // Verificar si hay suficiente stock
    if (cantidadEnCarrito + nuevoProducto.getCantidad() > stockDisponible) {
        return ResponseEntity.badRequest().body("❌ No hay suficiente stock disponible.");
    }

    // Si el producto ya está en el carrito, actualizamos la cantidad
    if (existente.isPresent()) {
        CarritoCompra item = existente.get();
        item.setCantidad(item.getCantidad() + nuevoProducto.getCantidad());
        carritoCompraDao.save(item);
        return ResponseEntity.ok("✅ Cantidad actualizada en el carrito.");
    } else {
        // Asignar el usuario al nuevo producto antes de guardarlo
        nuevoProducto.setUsuario(new Usuario(usuarioId));
        carritoCompraDao.save(nuevoProducto);
        return ResponseEntity.ok("✅ Producto agregado al carrito.");
    }
}*/
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
    // Actualizar cantidad de un producto en el carrito
    /*@PutMapping("/actualizar/{id}")
    public ResponseEntity<String> actualizarCantidad(@PathVariable int id, @RequestBody CarritoCompra productoActualizado) {
        Optional<CarritoCompra> existente = carritoCompraDao.findById(id);
        if (existente.isPresent()) {
            CarritoCompra item = existente.get();
            item.setCantidad(productoActualizado.getCantidad());
            carritoCompraDao.save(item);
            return ResponseEntity.ok("Cantidad actualizada");
        }
        return ResponseEntity.badRequest().body("Producto no encontrado en el carrito");
    }*//*
    @PutMapping("/actualizar")
public ResponseEntity<String> actualizarCantidadCarrito(
        @RequestHeader(value = "Authorization") String token,
        @RequestBody CarritoCompra productoActualizado) {

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

    // Verificar que el accesorio no sea nulo
    if (productoActualizado.getAccesorio() == null) {
        return ResponseEntity.badRequest().body("Error: El accesorio del producto es nulo");
    }

    int accesorioId = productoActualizado.getAccesorio().getId();

    // Buscar productos en el carrito del usuario
    List<CarritoCompra> carrito = carritoCompraDao.findByUsuarioId(usuarioId);

    Optional<CarritoCompra> existente = carrito.stream()
        .filter(item -> item.getAccesorio() != null && item.getAccesorio().getId() == accesorioId)
        .findFirst();

    if (existente.isPresent()) {
        CarritoCompra item = existente.get();
        item.setCantidad(productoActualizado.getCantidad());
        carritoCompraDao.save(item);
        return ResponseEntity.ok("Cantidad actualizada correctamente");
    } else {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Producto no encontrado en el carrito");
    }
}*/
    /*@PutMapping("/actualizar")
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

    if (productoActualizado.getAccesorio() == null /*|| productoActualizado.getAccesorio().getId() == null) {
        return ResponseEntity.badRequest().body("Error: Accesorio nulo o sin ID");
    }

    return ResponseEntity.ok("Solicitud válida, verificando en la BD...");
}*/
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
    // Eliminar producto del carrito
    /*@DeleteMapping("/eliminar/{id}")
    public ResponseEntity<String> eliminarDelCarrito(@PathVariable int id) {
        carritoCompraDao.deleteById(id);
        return ResponseEntity.ok("Producto eliminado del carrito");
    }*/

    /*@DeleteMapping("/eliminar/{accesorioId}")
public ResponseEntity<String> eliminarDelCarrito(
        @RequestHeader(value = "Authorization") String token,
        @PathVariable int accesorioId) {

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

    // Buscar producto en el carrito del usuario
    List<CarritoCompra> carrito = carritoCompraDao.findByUsuarioId(usuarioId);

    Optional<CarritoCompra> existente = carrito.stream()
        .filter(item -> item.getAccesorio() != null && item.getAccesorio().getId() == accesorioId)
        .findFirst();

    if (existente.isPresent()) {
        carritoCompraDao.delete(existente.get());
        return ResponseEntity.ok("Producto eliminado del carrito");
    } else {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Producto no encontrado en el carrito");
    }
}*/

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
    // Vaciar carrito del usuario
    @PostMapping("/procesar/{usuarioId}")
    public ResponseEntity<String> procesarCompra(@PathVariable int usuarioId) {
        List<CarritoCompra> carrito = carritoCompraDao.findByUsuario(usuarioDao.getUserById(usuarioId));
        carritoCompraDao.deleteAll(carrito);
        return ResponseEntity.ok("Compra procesada con éxito");
    }
    private boolean validarToken(String token){
        String userId = jwtUtil.getKey(token);
        //se puede verificar si este user esta en la db
        return userId != null;
    }
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
