package com.DriveZone.DriveZone.controllers;

import com.DriveZone.DriveZone.dao.AccesorioDao;
import com.DriveZone.DriveZone.dao.CarritoCompraDao;
import com.DriveZone.DriveZone.dao.OrdenCompraDaoImp;
import com.DriveZone.DriveZone.dao.UsuarioDao;
import com.DriveZone.DriveZone.models.Accesorio;
import com.DriveZone.DriveZone.models.CarritoCompra;
import com.DriveZone.DriveZone.models.OrdenCompra;
import com.DriveZone.DriveZone.models.Usuario;
import com.DriveZone.DriveZone.utils.JWTUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

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
    @PostMapping("/agregar")
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
    }*/
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
}
    // Eliminar producto del carrito
    /*@DeleteMapping("/eliminar/{id}")
    public ResponseEntity<String> eliminarDelCarrito(@PathVariable int id) {
        carritoCompraDao.deleteById(id);
        return ResponseEntity.ok("Producto eliminado del carrito");
    }*/
    @DeleteMapping("/eliminar/{accesorioId}")
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
