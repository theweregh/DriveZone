package com.DriveZone.DriveZone.repository;

import com.DriveZone.DriveZone.models.Cliente;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AccesorioRepository extends JpaRepository<Cliente, Integer> {
}
