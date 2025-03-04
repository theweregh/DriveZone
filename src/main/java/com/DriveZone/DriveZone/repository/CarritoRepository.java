package com.DriveZone.DriveZone.repository;

import com.DriveZone.DriveZone.models.CarritoCompra;
import com.DriveZone.DriveZone.models.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CarritoRepository extends JpaRepository<CarritoCompra, Integer> {
    Optional<CarritoCompra> findByUsuario(Usuario usuario);
}
