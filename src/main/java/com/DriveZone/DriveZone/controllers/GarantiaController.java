package com.DriveZone.DriveZone.controllers;

import com.DriveZone.DriveZone.models.Garantia;
import com.DriveZone.DriveZone.models.GarantiaEstado;
import com.DriveZone.DriveZone.services.EmailService;
import com.DriveZone.DriveZone.services.GarantiaService;
import jakarta.mail.MessagingException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/garantias")
@CrossOrigin(origins = "*")
public class GarantiaController {
    @Autowired
    private GarantiaService garantiaService;
    @Autowired
    private EmailService emailService;

    @GetMapping
    public List<Garantia> listar() {
        return garantiaService.listarTodas();
    }

    @GetMapping("/{id}")
    public Optional<Garantia> buscar(@PathVariable Integer id) {
        return garantiaService.buscarPorId(id);
    }

    @PostMapping
    public Garantia crear(@RequestBody Garantia garantia) {
        return garantiaService.guardar(garantia);
    }

    @DeleteMapping("/{id}")
    public void eliminar(@PathVariable Integer id) {
        garantiaService.eliminar(id);
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> actualizarEstado(@PathVariable Integer id, @RequestBody GarantiaEstado nuevoEstado) {
        try {
            Garantia actualizada = garantiaService.actualizarEstado(id, nuevoEstado);

            // Obtener correo del cliente
            String correoCliente = actualizada.getFactura()
                    .getOrdenCompra()
                    .getUsuario()
                    .getCorreo();
            String estado = "";
            // Mensaje personalizado según el estado
            String mensaje = "";
            switch (nuevoEstado.name().toLowerCase()) {
                case "garantia":
                    mensaje = "Tu producto será reemplazado conforme a la política de garantía.";
                    estado = "garantia";
                    break;
                case "devolucion":
                    mensaje = "Se procederá con la devolución del dinero en los próximos días.";
                    estado = "devolucion";
                    break;
                case "rechazada":
                    mensaje = "Lamentamos informarte que no cumple con las condiciones para aplicar garantía.";
                    estado = "rechazada";
                    break;
                default:
                    mensaje = "Tu solicitud ha sido procesada.";
                    estado = "procesada";
                    break;
            }

            // Enviar correo
            emailService.enviarRespuestaGarantia(correoCliente, estado, mensaje);

            return ResponseEntity.ok(actualizada);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        } catch (MessagingException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error al enviar el correo.");
        }
    }
}
