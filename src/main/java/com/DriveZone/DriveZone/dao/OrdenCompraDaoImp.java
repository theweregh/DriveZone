package com.DriveZone.DriveZone.dao;

import com.DriveZone.DriveZone.models.OrdenCompra;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Repository
@Transactional
public class OrdenCompraDaoImp {
     @Autowired
    private OrdenCompraDao ordenCompraDao;

    public List<OrdenCompra> obtenerTodasLasOrdenes() {
        return ordenCompraDao.findAll();
    }

    public Optional<OrdenCompra> obtenerOrdenPorId(int id) {
        return ordenCompraDao.findById(id);
    }

    public OrdenCompra guardarOrden(OrdenCompra orden) {
        return ordenCompraDao.save(orden);
    }

    public void eliminarOrden(int id) {
        ordenCompraDao.deleteById(id);
    }


}
