package com.DriveZone.DriveZone.models;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Table(name = "historialAccesorio")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class HistorialAccesorio  {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int idHistorialAccesorio;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private Accion accion;

    @Column(nullable = false)
    private String nombreAccesorio;

    @Column(nullable = false)
    private int idAccesorio;

    @Column(nullable = false)
    private LocalDateTime fecha;

    public HistorialAccesorio(Accion accion, String nombreAccesorio, int idAccesorio) {
        this.accion = accion;
        this.nombreAccesorio = nombreAccesorio;
        this.idAccesorio = idAccesorio;
        this.fecha = LocalDateTime.now();
    }
}
