package com.DriveZone.DriveZone.controllers;

import com.DriveZone.DriveZone.dao.UsuarioDao;
import com.DriveZone.DriveZone.models.Factura;
import com.DriveZone.DriveZone.models.Usuario;
import com.DriveZone.DriveZone.services.EmailService;
import com.DriveZone.DriveZone.services.FacturaService;
import com.DriveZone.DriveZone.utils.JWTUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

/**
 * Controlador REST para la gestión de facturas en el sistema.
 * Proporciona endpoints para crear, eliminar, obtener, descargar en formato PDF
 * y enviar facturas por correo electrónico.
 */
@RestController
@RequestMapping("/api/facturas")
public class FacturaController {
    @Autowired
    private FacturaService facturaService;
    @Autowired
    private EmailService emailService;
    @Autowired
    private JWTUtil jwtUtil;
    @Autowired
    private UsuarioDao usuarioDao;

    /**
     * Obtiene todas las facturas registradas en el sistema.
     *
     * @return Lista de objetos {@link Factura}.
     */
    @GetMapping
    public List<Factura> obtenerTodas() {
        return facturaService.obtenerTodas();
    }

    /**
     * Obtiene una factura por su ID.
     *
     * @param id Identificador de la factura.
     * @return Un {@link Optional} con la factura si se encuentra, o vacío si no existe.
     */
    @GetMapping("/{id}")
    public Optional<Factura> obtenerPorId(@PathVariable Integer id) {
        return facturaService.obtenerPorId(id);
    }

    /**
     * Elimina una factura del sistema por su ID.
     *
     * @param id Identificador de la factura a eliminar.
     */
    @DeleteMapping("/{id}")
    public void eliminarFactura(@PathVariable Integer id) {
        facturaService.eliminarFactura(id);
    }

    /**
     * Genera una nueva factura basada en una orden de compra específica.
     *
     * @param idOrdenCompra ID de la orden de compra asociada a la factura.
     * @param facturaData   Datos de la factura a generar.
     * @return La factura creada.
     */
    @PostMapping("/{idOrdenCompra}")
    public ResponseEntity<Factura> crearFactura(
            @PathVariable int idOrdenCompra,
            @RequestBody Factura facturaData) {
        Factura factura = facturaService.generarFactura(facturaData, idOrdenCompra);
        return ResponseEntity.ok(factura);
    }

    /**
     * Descarga una factura en formato PDF.
     *
     * @param id Identificador de la factura.
     * @return Un archivo PDF con los datos de la factura.
     * @throws IOException Si ocurre un error al generar el archivo PDF.
     */
    @GetMapping("/{id}/pdf")
    public ResponseEntity<byte[]> descargarFactura(@PathVariable int id) throws IOException {
        byte[] pdfBytes = facturaService.generarFacturaPDF(id);
        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=factura_" + id + ".pdf")
                .contentType(MediaType.APPLICATION_PDF)
                .body(pdfBytes);
    }

    /**
     * Envía una factura por correo electrónico en formato PDF.
     *
     * @param id    Identificador de la factura a enviar.
     * @param email Dirección de correo electrónico del destinatario.
     * @return Respuesta indicando el resultado de la operación.
     */
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
    /**
     * Retorna la lista de facturas asociadas al usuario autenticado.
     *
     * @param token Token JWT enviado en el header Authorization.
     * @return Lista de facturas o error si no autorizado.
     */
    @GetMapping("/mis-facturas")
    public ResponseEntity<?> obtenerFacturasPorUsuario(@RequestHeader(value = "Authorization") String token) {
        // Validar token
        String userId = jwtUtil.getKey(token);
        if (userId == null) {
            return ResponseEntity.status(401).body("No autorizado: token inválido");
        }

        // Obtener usuario autenticado
        Usuario usuario = usuarioDao.getUserById(Integer.parseInt(userId));
        if (usuario == null) {
            return ResponseEntity.status(404).body("Usuario no encontrado");
        }

        // Obtener facturas por el username (o puedes usar el ID si prefieres)
        List<Factura> facturas = facturaService.obtenerFacturasPorUsuario(usuario.getUsername());

        return ResponseEntity.ok(facturas);
    }
}
