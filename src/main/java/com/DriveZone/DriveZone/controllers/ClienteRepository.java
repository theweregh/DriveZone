package com.DriveZone.DriveZone.controllers;

import com.DriveZone.DriveZone.models.Cliente;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ClienteRepository extends JpaRepository<Cliente, Long> {
    List<Cliente> findByNombreContainingIgnoreCaseOrApellidoContainingIgnoreCaseOrCedula(String nombre, String apellido, String cedula);
    List<Cliente> findByNombre(String nombre);

}
