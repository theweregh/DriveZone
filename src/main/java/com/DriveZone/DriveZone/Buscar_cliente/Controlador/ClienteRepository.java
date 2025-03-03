package com.DriveZone.DriveZone.Buscar_cliente.Controlador;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.DriveZone.DriveZone.Buscar_cliente.Modelo.Cliente;

@Repository
public interface ClienteRepository extends JpaRepository<Cliente, Long> {}
