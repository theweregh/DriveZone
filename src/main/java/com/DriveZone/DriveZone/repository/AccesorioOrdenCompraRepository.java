package com.DriveZone.DriveZone.repository;

import com.DriveZone.DriveZone.models.AccesorioHasOrdenCompra;
import com.DriveZone.DriveZone.models.AccesorioHasOrdenCompraId;
import com.DriveZone.DriveZone.models.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AccesorioOrdenCompraRepository extends JpaRepository<AccesorioHasOrdenCompra , AccesorioHasOrdenCompraId   > {
    // Buscar por id_ordencompra dentro de la clave compuesta
    List<AccesorioHasOrdenCompra> findById_IdOrdenCompra(int idOrdenCompra);
    
}
