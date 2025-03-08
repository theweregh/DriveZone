package com.DriveZone.DriveZone.dao;

import com.DriveZone.DriveZone.models.Accesorio;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Repositorio para la entidad Accesorio.
 * Extiende JpaRepository para proporcionar operaciones CRUD estándar.
 */
@Repository
public interface AccesorioDao extends JpaRepository<Accesorio, Integer> {

}
