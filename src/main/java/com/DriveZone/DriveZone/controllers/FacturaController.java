package com.DriveZone.DriveZone.controllers;

import com.DriveZone.DriveZone.models.Factura;
import com.DriveZone.DriveZone.services.EmailService;
import com.DriveZone.DriveZone.services.FacturaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/facturas")
public class FacturaController {
    @Autowired
    private FacturaService facturaService;
     @Autowired
    private EmailService emailService;

    @GetMapping
    public List<Factura> obtenerTodas() {
        return facturaService.obtenerTodas();
    }

    @GetMapping("/{id}")
    public Optional<Factura> obtenerPorId(@PathVariable Integer id) {
        return facturaService.obtenerPorId(id);
    }
/*
    @PostMapping
    public Factura guardarFactura(@RequestBody Factura factura) {
        return facturaService.guardarFactura(factura);
    }*/

    @DeleteMapping("/{id}")
    public void eliminarFactura(@PathVariable Integer id) {
        facturaService.eliminarFactura(id);
    }
    @PostMapping("/{idOrdenCompra}")
    public ResponseEntity<Factura> crearFactura(
        @PathVariable int idOrdenCompra,
        @RequestBody Factura facturaData) {
        Factura factura = facturaService.generarFactura(facturaData, idOrdenCompra);
        return ResponseEntity.ok(factura);
    }
    @GetMapping("/{id}/pdf")
    public ResponseEntity<byte[]> descargarFactura(@PathVariable int id) throws IOException {
        byte[] pdfBytes = facturaService.generarFacturaPDF(id);
        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=factura_" + id + ".pdf")
                .contentType(MediaType.APPLICATION_PDF)
                .body(pdfBytes);
    }
    

    @PostMapping("/{id}/enviar-correo")
    public ResponseEntity<String> enviarFacturaPorCorreo(@PathVariable int id, @RequestParam String email) {
        try {
            System.out.println("📨 Enviando factura ID: " + id + " al correo: " + email);
            
            byte[] pdfBytes = facturaService.generarFacturaPDF(id);
            emailService.enviarFacturaPorCorreo(email, pdfBytes, "factura_" + id + ".pdf");
    
            System.out.println("✅ Factura enviada correctamente a " + email);
            return ResponseEntity.ok("Factura enviada a: " + email);
        } catch (Exception e) {
            System.err.println("❌ Error al enviar correo: " + e.getMessage());
            e.printStackTrace();
            return ResponseEntity.status(500).body("Error al enviar el correo: " + e.getMessage());
        }
    }

    
    

}
