package com.DriveZone.DriveZone.controllers;

import com.DriveZone.DriveZone.models.HistorialAccesorio;
import com.DriveZone.DriveZone.services.HistorialAccesorioService;
import com.itextpdf.text.DocumentException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
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

    private ResponseEntity<byte[]> generarRespuestaPdf(byte[] contenido, String nombreArchivo) {
        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=" + nombreArchivo)
                .contentType(MediaType.APPLICATION_PDF)
                .body(contenido);
    }

    @GetMapping("/pdf")
    public ResponseEntity<byte[]> descargarHistorial() throws IOException, DocumentException {
        return generarRespuestaPdf(historialAccesorioService.generarPdfHistorial(), "historial.pdf");
    }

    @GetMapping("/pdf/agregados")
    public ResponseEntity<byte[]> descargarHistorialAgregados() throws IOException, DocumentException {
        return generarRespuestaPdf(historialAccesorioService.generarPdfAgregados(), "historial_agregados.pdf");
    }

    @GetMapping("/pdf/eliminados")
    public ResponseEntity<byte[]> descargarHistorialEliminados() throws IOException, DocumentException {
        return generarRespuestaPdf(historialAccesorioService.generarPdfEliminados(), "historial_eliminados.pdf");
    }
    @GetMapping
public ResponseEntity<List<HistorialAccesorio>> obtenerHistorial() {
    return ResponseEntity.ok(historialAccesorioService.obtenerHistorial());
}
}
