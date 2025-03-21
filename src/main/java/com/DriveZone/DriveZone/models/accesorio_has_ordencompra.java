package com.DriveZone.DriveZone.models;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Embeddable
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class accesorio_has_ordencompra {
    private int id_accesorio;
    private int id_ordencompra;
    @Column(name = "cantidad")
    private int cantidad;
}
