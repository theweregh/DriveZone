package com.DriveZone.DriveZone.dao;

import com.DriveZone.DriveZone.models.Accesorio;
import com.DriveZone.DriveZone.models.CarritoCompra;
import com.DriveZone.DriveZone.models.OrdenCompra;
import com.DriveZone.DriveZone.models.Usuario;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.FluentQuery;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.function.Function;

@Repository
@Transactional
public class CarritoCompraDaoImp{
     /*@Autowired
    private CarritoCompraDao carritoCompraDao;

    // 🔹 Agregar accesorio al carrito (Si ya existe, aumenta la cantidad)
    public CarritoCompra agregarAlCarrito(Usuario usuario, Accesorio accesorio, int cantidad) {
        Optional<CarritoCompra> carritoExistente = carritoCompraDao.findByUsuarioAndAccesorio_Id(usuario, accesorio.getId());

        CarritoCompra carritoCompra;
        if (carritoExistente.isPresent()) {
            carritoCompra = carritoExistente.get();
            carritoCompra.setCantidad(carritoCompra.getCantidad() + cantidad);
        } else {
            carritoCompra = new CarritoCompra();
            carritoCompra.setUsuario(usuario);
            carritoCompra.setAccesorio(accesorio);
            carritoCompra.setCantidad(cantidad);
        }
        return carritoCompraDao.save(carritoCompra);
    }

    // 🔹 Obtener el carrito de un usuario
    public List<CarritoCompra> obtenerCarritoPorUsuario(Usuario usuario) {
        return carritoCompraDao.findByUsuario(usuario).stream().toList();
    }

    // 🔹 Eliminar un producto del carrito
    public void eliminarDelCarrito(Usuario usuario, Accesorio accesorio) {
        Optional<CarritoCompra> carritoExistente = carritoCompraDao.findByUsuarioAndAccesorio_Id(usuario, accesorio.getId());
        carritoExistente.ifPresent(carritoCompraDao::delete);
    }

    // 🔹 Vaciar el carrito de un usuario
    public void vaciarCarrito(Usuario usuario) {
        List<CarritoCompra> carrito = obtenerCarritoPorUsuario(usuario);
        carritoCompraDao.deleteAll(carrito);
    }*/
    @PersistenceContext
     EntityManager entityManager;

    public List<OrdenCompra> getOrdenesCompra() {
        String query = "FROM OrdenCompra";
        return entityManager.createQuery(query, OrdenCompra.class).getResultList();
    }

    public OrdenCompra getOrdenCompraById(int id) {
        return entityManager.find(OrdenCompra.class, id);
    }

    public void deleteOrdenCompra(int id) {
        OrdenCompra orden = entityManager.find(OrdenCompra.class, id);
        if (orden != null) {
            entityManager.remove(orden);
        }
    }

    public void registrar(OrdenCompra ordenCompra) {
        entityManager.merge(ordenCompra);
    }

}
