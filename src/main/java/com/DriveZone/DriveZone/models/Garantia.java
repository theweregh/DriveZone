package com.DriveZone.DriveZone.models;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

/**
 * Entidad que representa una solicitud de garantía realizada por un cliente.
 * Cada garantía está asociada a una factura y tiene un estado que indica su proceso actual.
 *
 * @author DriveZone Team
 * @version 1.1
 * @since 2025-04-27
 */
@Entity
@Table(name = "garantia")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Garantia {
    /**
     * Identificador único de la garantía.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer idGarantia;
    /**
     * Factura asociada a la garantía.
     */
    @ManyToOne
    @JoinColumn(name = "idFactura", nullable = false)
    private Factura factura;
    /**
     * Estado actual de la garantía.
     */
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private GarantiaEstado estado;
    /**
     * Fecha y hora en que se realizó la solicitud de garantía.
     * Se inicializa automáticamente al momento de crear la garantía.
     */
    @Column(nullable = false)
    private LocalDateTime fechaSolicitud = LocalDateTime.now();
}
