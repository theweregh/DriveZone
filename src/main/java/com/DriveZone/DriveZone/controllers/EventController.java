package com.drivezone.controllers;

import com.drivezone.models.Evento;
import com.drivezone.services.EventService;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.util.List;

@RestController
public class EventController {
    private final EventService service;

    public EventController(EventService service) {
        this.service = service;
    }

    @GetMapping("/api/eventos")
    public List<Evento> getEventos(
            @RequestParam(required = false) String fechaInicio,
            @RequestParam(required = false) String fechaFin) {
        return service.obtenerEventos(fechaInicio, fechaFin);
    }

    @GetMapping("/api/eventos/pdf")
    public ResponseEntity<byte[]> descargarPdf(
            @RequestParam(required = false) String fechaInicio,
            @RequestParam(required = false) String fechaFin) throws IOException {
        byte[] pdf = service.generarPdfEventos(fechaInicio, fechaFin);
        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=control_eventos.pdf")
                .contentType(MediaType.APPLICATION_PDF)
                .body(pdf);
    }
}