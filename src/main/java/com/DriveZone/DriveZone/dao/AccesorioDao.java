package com.DriveZone.DriveZone.dao;

import com.DriveZone.DriveZone.models.Accesorio;
import com.DriveZone.DriveZone.models.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
@Repository
public interface AccesorioDao extends JpaRepository<Accesorio, String> {

}
