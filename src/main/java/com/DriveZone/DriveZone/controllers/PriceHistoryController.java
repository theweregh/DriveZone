package com.drivezone.controllers;

import com.drivezone.models.PriceHistory;
import com.drivezone.services.PriceHistoryService;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.util.List;

@RestController
public class PriceHistoryController {
    private final PriceHistoryService service;

    public PriceHistoryController(PriceHistoryService service) {
        this.service = service;
    }

    @GetMapping("/api/precios/historial")
    public List<PriceHistory> getHistorial(
            @RequestParam(required = false) String fechaInicio,
            @RequestParam(required = false) String fechaFin) {
        return service.obtenerHistorial(fechaInicio, fechaFin);
    }

    @GetMapping("/api/precios/historial/pdf")
    public ResponseEntity<byte[]> descargarPdf(
            @RequestParam(required = false) String fechaInicio,
            @RequestParam(required = false) String fechaFin) throws IOException {
        byte[] pdf = service.generarPdfHistorial(fechaInicio, fechaFin);
        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=historial_precios.pdf")
                .contentType(MediaType.APPLICATION_PDF)
                .body(pdf);
    }
}