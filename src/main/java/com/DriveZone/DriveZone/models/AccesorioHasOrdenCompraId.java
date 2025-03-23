package com.DriveZone.DriveZone.models;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.*;

import java.io.Serializable;

@Embeddable
@EqualsAndHashCode
@Getter
@Setter
@NoArgsConstructor
@ToString
public class AccesorioHasOrdenCompraId implements Serializable {
    @Column(name = "id_accesorio")
    private int idAccesorio;

    @Column(name = "id_ordencompra")
    private int idOrdenCompra;

}
