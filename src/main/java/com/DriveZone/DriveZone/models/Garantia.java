package com.DriveZone.DriveZone.models;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Table(name = "garantia")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Garantia {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer idGarantia;

    @ManyToOne
    @JoinColumn(name = "idFactura", nullable = false)
    private Factura factura;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private GarantiaEstado estado;

    @Column(nullable = false)
    private LocalDateTime fechaSolicitud = LocalDateTime.now();
}
