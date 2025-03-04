package com.DriveZone.DriveZone.dao;

import com.DriveZone.DriveZone.models.OrdenCompra;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface OrdenCompraDao extends JpaRepository<OrdenCompra, Integer> {

}
