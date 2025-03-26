package com.DriveZone.DriveZone.controllers;

import com.DriveZone.DriveZone.models.HistorialAccesorio;
import com.DriveZone.DriveZone.services.HistorialAccesorioService;
import com.itextpdf.text.DocumentException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/api/historial-accesorios")
@CrossOrigin("*")
public class HistorialAccesorioController {
    private final HistorialAccesorioService historialAccesorioService;

    public HistorialAccesorioController(HistorialAccesorioService historialAccesorioService) {
        this.historialAccesorioService = historialAccesorioService;
    }

    @GetMapping
    public ResponseEntity<List<HistorialAccesorio>> obtenerHistorial() {
        return ResponseEntity.ok(historialAccesorioService.obtenerHistorial());
    }

    @GetMapping("/descargar-pdf")
    public ResponseEntity<byte[]> descargarPdf() {
        try {
            byte[] pdf = historialAccesorioService.generarPdfHistorial();
            HttpHeaders headers = new HttpHeaders();
            headers.add("Content-Disposition", "attachment; filename=historial_accesorios.pdf");
            return new ResponseEntity<>(pdf, headers, HttpStatus.OK);
        } catch (IOException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        } catch (DocumentException e) {
            throw new RuntimeException(e);
        }
    }
}
