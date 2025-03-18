package com.DriveZone.DriveZone.services;

import com.DriveZone.DriveZone.dao.AccesorioDao;
import com.DriveZone.DriveZone.dao.CarritoCompraDao;
import com.DriveZone.DriveZone.dao.OrdenCompraDao;
import com.DriveZone.DriveZone.models.Accesorio;
import com.DriveZone.DriveZone.models.CarritoCompra;
import com.DriveZone.DriveZone.models.OrdenCompra;
import com.DriveZone.DriveZone.models.Usuario;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
public class OrdenCompraService {
    @Autowired
    private OrdenCompraDao ordenCompraDao;

    @Autowired
    private CarritoCompraDao carritoCompraDao;

    @Autowired
    private AccesorioDao accesorioDao;

    /**
     * Obtiene todas las órdenes de compra registradas en la base de datos.
     * @return Lista de órdenes de compra.
     */
    public List<OrdenCompra> obtenerTodasLasOrdenes() {
        return ordenCompraDao.findAll();
    }

    /**
     * Obtiene una orden de compra por su ID.
     * @param id ID de la orden de compra.
     * @return La orden de compra si existe, o null si no se encuentra.
     */
    public Optional<OrdenCompra> obtenerOrdenPorId(int id) {
        return ordenCompraDao.findById(id);
    }

    /**
     * Guarda una nueva orden de compra en la base de datos.
     * @param ordenCompra Orden de compra a guardar.
     * @return La orden de compra guardada.
     */
    @Transactional
    public OrdenCompra crearOrdenCompra(OrdenCompra ordenCompra) {
        ordenCompra.setFecha(new Date()); // Asignar la fecha actual
        return ordenCompraDao.save(ordenCompra);
    }

    /**
     * Procesa la compra de un usuario, creando una orden y eliminando los elementos del carrito.
     * @param usuario Usuario que realiza la compra.
     */
    @Transactional
    public void procesarCompra(Usuario usuario) {
        List<CarritoCompra> carrito = carritoCompraDao.findByUsuario(usuario);
        if (carrito.isEmpty()) {
            throw new IllegalStateException("El carrito está vacío");
        }

        OrdenCompra orden = new OrdenCompra();
        orden.setUsuario(usuario);
        orden.setFecha(new Date());
        orden.setCarritoCompra(carrito);
        //orden.setProductos(carrito.stream().map(CarritoCompra::getAccesorio).toList());

        ordenCompraDao.save(orden);

        // Actualizar stock de accesorios comprados
        for (CarritoCompra item : carrito) {
            Accesorio accesorio = item.getAccesorio();
            int nuevoStock = accesorio.getStock() - item.getCantidad();
            if (nuevoStock < 0) {
                throw new IllegalStateException("Stock insuficiente para " + accesorio.getNombre());
            }
            accesorio.setStock(nuevoStock);
            accesorioDao.save(accesorio);
        }

        // Eliminar elementos del carrito tras procesar la compra
        carritoCompraDao.deleteAll(carrito);
    }

    /**
     * Elimina una orden de compra por su ID.
     * @param id ID de la orden de compra a eliminar.
     * @return `true` si se eliminó correctamente, `false` si no se encontró.
     */
    @Transactional
    public boolean eliminarOrden(int id) {
        if (ordenCompraDao.existsById(id)) {
            ordenCompraDao.deleteById(id);
            return true;
        }
        return false;
    }
}
