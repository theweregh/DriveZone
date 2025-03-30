package com.DriveZone.DriveZone.controllers;

import com.DriveZone.DriveZone.models.HistorialAccesorio;
import com.DriveZone.DriveZone.services.HistorialAccesorioService;
import com.itextpdf.text.DocumentException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.util.List;

/**
 * Controlador REST para gestionar el historial de accesorios en el sistema.
 * Proporciona endpoints para obtener el historial completo y generar reportes en formato PDF.
 */
@RestController
@RequestMapping("/api/historial-accesorios")
@CrossOrigin("*")
public class HistorialAccesorioController {
    private final HistorialAccesorioService historialAccesorioService;

    /**
     * Constructor del controlador que inyecta el servicio de historial de accesorios.
     *
     * @param historialAccesorioService Servicio para manejar la lógica del historial de accesorios.
     */
    public HistorialAccesorioController(HistorialAccesorioService historialAccesorioService) {
        this.historialAccesorioService = historialAccesorioService;
    }

    /**
     * Genera una respuesta HTTP con un archivo PDF adjunto.
     *
     * @param contenido     Contenido del archivo PDF en bytes.
     * @param nombreArchivo Nombre del archivo que se enviará en la respuesta.
     * @return {@link ResponseEntity} con el archivo PDF listo para su descarga.
     */
    private ResponseEntity<byte[]> generarRespuestaPdf(byte[] contenido, String nombreArchivo) {
        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=" + nombreArchivo)
                .contentType(MediaType.APPLICATION_PDF)
                .body(contenido);
    }

    /**
     * Genera y descarga un informe PDF con el historial completo de accesorios.
     *
     * @return {@link ResponseEntity} con el archivo PDF del historial de accesorios.
     * @throws IOException       Si ocurre un error al generar el archivo.
     * @throws DocumentException Si ocurre un error al crear el documento PDF.
     */
    @GetMapping("/pdf")
    public ResponseEntity<byte[]> descargarHistorial() throws IOException, DocumentException {
        return generarRespuestaPdf(historialAccesorioService.generarPdfHistorial(), "historial.pdf");
    }

    /**
     * Genera y descarga un informe PDF con el historial de accesorios agregados.
     *
     * @return {@link ResponseEntity} con el archivo PDF de accesorios agregados.
     * @throws IOException       Si ocurre un error al generar el archivo.
     * @throws DocumentException Si ocurre un error al crear el documento PDF.
     */
    @GetMapping("/pdf/agregados")
    public ResponseEntity<byte[]> descargarHistorialAgregados() throws IOException, DocumentException {
        return generarRespuestaPdf(historialAccesorioService.generarPdfAgregados(), "historial_agregados.pdf");
    }

    /**
     * Genera y descarga un informe PDF con el historial de accesorios eliminados.
     *
     * @return {@link ResponseEntity} con el archivo PDF de accesorios eliminados.
     * @throws IOException       Si ocurre un error al generar el archivo.
     * @throws DocumentException Si ocurre un error al crear el documento PDF.
     */
    @GetMapping("/pdf/eliminados")
    public ResponseEntity<byte[]> descargarHistorialEliminados() throws IOException, DocumentException {
        return generarRespuestaPdf(historialAccesorioService.generarPdfEliminados(), "historial_eliminados.pdf");
    }

    /**
     * Obtiene el historial completo de accesorios en formato JSON.
     *
     * @return {@link ResponseEntity} con la lista de objetos {@link HistorialAccesorio}.
     */
    @GetMapping
    public ResponseEntity<List<HistorialAccesorio>> obtenerHistorial() {
        return ResponseEntity.ok(historialAccesorioService.obtenerHistorial());
    }
}
