package com.DriveZone.DriveZone.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.Cascade;

@Entity
@Getter @Setter
@AllArgsConstructor @NoArgsConstructor
public class CarritoCompra {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "idCarritoCompra")
    private int id;
    @Column(name = "cantidad", nullable = true)
    int cantidad;
    /*@ManyToOne
    @JoinColumn(name = "Accesorio_idAccesorio", nullable = false)
    private Accesorio accesorio;*/
    @ManyToOne
    @JoinColumn(name = "Accesorio_idAccesorio", nullable = true) // Permite valores nulos
    private Accesorio accesorio;
    @ManyToOne
    @JoinColumn(name = "id_usuario", nullable = false)
    //@Cascade(CascadeType.PERSIST) // Esto hace que el usuario se guarde junto con el carrito
    @JsonIgnore
    private Usuario usuario;
}
