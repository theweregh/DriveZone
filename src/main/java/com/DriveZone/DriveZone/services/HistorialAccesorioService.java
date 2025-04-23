package com.DriveZone.DriveZone.services;

import com.DriveZone.DriveZone.models.Accion;
import com.DriveZone.DriveZone.models.HistorialAccesorio;
import com.DriveZone.DriveZone.repository.HistorialAccesorioRepository;
import com.itextpdf.text.*;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import org.springframework.stereotype.Service;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Servicio para gestionar el historial de accesorios y generar reportes en PDF.
 *
 * @author DriveZone Team
 * @version 1.1
 * @since 2025-03-30
 */
@Service
public class HistorialAccesorioService {
    private final HistorialAccesorioRepository historialAccesorioRepository;

    public HistorialAccesorioService(HistorialAccesorioRepository historialAccesorioRepository) {
        this.historialAccesorioRepository = historialAccesorioRepository;
    }

    // Método para agregar el logo y el formato mejorado
    private byte[] generarPdf(List<HistorialAccesorio> historial, String titulo) throws IOException, DocumentException {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        Document document = new Document();
        PdfWriter.getInstance(document, outputStream);
        document.open();

        // Agregar logo
        try {
            String logoPath = "src/main/resources/static/img/DriveZone.png"; // Especifica la ruta de tu logo
            Image logo = Image.getInstance(logoPath);
            logo.setAbsolutePosition(15, 740);  // Establecer la posición del logo
            logo.scaleToFit(100, 100);          // Ajustar el tamaño
            document.add(logo);
        } catch (Exception e) {
            System.out.println("Error al agregar el logo: " + e.getMessage());
        }

        // Título centrado
        Paragraph title = new Paragraph(titulo, FontFactory.getFont("Helvetica", 18, Font.BOLD));
        title.setAlignment(Paragraph.ALIGN_CENTER);
        document.add(title);

        // Fecha
        String fechaHoy = new java.util.Date().toString(); // Fecha del reporte
        Paragraph fecha = new Paragraph("Fecha del reporte: " + fechaHoy);
        fecha.setAlignment(Paragraph.ALIGN_CENTER);
        document.add(fecha);

        // Espacio entre el título y la tabla
        document.add(new Paragraph("\n"));

        // Insertar la tabla con los datos
        PdfPTable table = new PdfPTable(4);  // 4 columnas
        table.setWidths(new float[]{1, 2, 3, 2});  // Ajustar el ancho de las columnas
        table.setWidthPercentage(100);  // Ocupa todo el ancho de la página

        // Agregar encabezados a la tabla
        table.addCell("ID");
        table.addCell("Acción");
        table.addCell("Nombre Accesorio");
        table.addCell("Fecha");

        // Llenar la tabla con los datos del historial
        for (HistorialAccesorio item : historial) {
            table.addCell(String.valueOf(item.getIdHistorialAccesorio()));
            table.addCell(item.getAccion().name());
            table.addCell(item.getNombreAccesorio());
            table.addCell(item.getFecha().toString());
        }

        // Agregar la tabla al documento
        document.add(table);

        document.close();
        return outputStream.toByteArray();
    }

    // Método para generar el PDF completo del historial
    public byte[] generarPdfHistorial() throws IOException, DocumentException {
        return generarPdf(obtenerHistorial(), "Historial de Accesorios");
    }

    // Método para generar el PDF de accesorios agregados
    public byte[] generarPdfAgregados() throws IOException, DocumentException {
        List<HistorialAccesorio> agregados = obtenerHistorial().stream()
                .filter(h -> h.getAccion() == Accion.AGREGADO)
                .collect(Collectors.toList());
        return generarPdf(agregados, "Historial de Accesorios Agregados");
    }

    // Método para generar el PDF de accesorios eliminados
    public byte[] generarPdfEliminados() throws IOException, DocumentException {
        List<HistorialAccesorio> eliminados = obtenerHistorial().stream()
                .filter(h -> h.getAccion() == Accion.ELIMINADO)
                .collect(Collectors.toList());
        return generarPdf(eliminados, "Historial de Accesorios Eliminados");
    }

    // Método para obtener el historial desde el repositorio
    public List<HistorialAccesorio> obtenerHistorial() {
        return historialAccesorioRepository.findAll();
    }
}
