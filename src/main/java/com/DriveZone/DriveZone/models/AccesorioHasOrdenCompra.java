package com.DriveZone.DriveZone.models;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "accesorio_has_ordencompra")
@Getter
@Setter
@NoArgsConstructor
public class AccesorioHasOrdenCompra  {
    @EmbeddedId
    private AccesorioHasOrdenCompraId id;
    @ManyToOne
    @MapsId("idAccesorio") // Relaciona con la clave primaria compuesta
    @JoinColumn(name = "accesorio_idAccesorio", nullable = false)
    private Accesorio accesorio;

    @ManyToOne
    @MapsId("idOrdenCompra") // Relaciona con la clave primaria compuesta
    @JoinColumn(name = "ordencompra_idOrdenCompra", nullable = false)
    private OrdenCompra ordenCompra;

    @Column(name = "cantidad", nullable = false)
    private int cantidad;


    
}
