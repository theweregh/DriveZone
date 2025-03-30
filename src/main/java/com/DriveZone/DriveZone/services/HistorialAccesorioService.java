package com.DriveZone.DriveZone.services;

import com.DriveZone.DriveZone.models.Accion;
import com.DriveZone.DriveZone.models.HistorialAccesorio;
import com.DriveZone.DriveZone.repository.HistorialAccesorioRepository;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Paragraph;
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

    /**
     * Constructor del servicio que inyecta el repositorio de historial de accesorios.
     *
     * @param historialAccesorioRepository Repositorio para acceder al historial de accesorios.
     */
    public HistorialAccesorioService(HistorialAccesorioRepository historialAccesorioRepository) {
        this.historialAccesorioRepository = historialAccesorioRepository;
    }

    /**
     * Obtiene la lista completa del historial de accesorios.
     *
     * @return Lista de {@link HistorialAccesorio} con todos los registros almacenados.
     */
    public List<HistorialAccesorio> obtenerHistorial() {
        return historialAccesorioRepository.findAll();
    }

    /**
     * Genera un documento PDF con el historial de accesorios.
     *
     * @param historial Lista de accesorios a incluir en el PDF.
     * @param titulo    Título del documento.
     * @return Un array de bytes que representa el contenido del PDF.
     * @throws IOException       Si ocurre un error al escribir los datos.
     * @throws DocumentException Si ocurre un error al generar el documento PDF.
     */
    private byte[] generarPdf(List<HistorialAccesorio> historial, String titulo) throws IOException, DocumentException {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        Document document = new Document();
        PdfWriter.getInstance(document, outputStream);
        document.open();

        document.add(new Paragraph(titulo));
        PdfPTable table = new PdfPTable(4);
        table.addCell("ID");
        table.addCell("Acción");
        table.addCell("Nombre Accesorio");
        table.addCell("Fecha");

        for (HistorialAccesorio item : historial) {
            table.addCell(String.valueOf(item.getIdHistorialAccesorio()));
            table.addCell(item.getAccion().name());
            table.addCell(item.getNombreAccesorio());
            table.addCell(item.getFecha().toString());
        }

        document.add(table);
        document.close();
        return outputStream.toByteArray();
    }

    /**
     * Genera un PDF con todo el historial de accesorios.
     *
     * @return Un array de bytes representando el PDF del historial completo.
     * @throws IOException       Si ocurre un error al generar el archivo.
     * @throws DocumentException Si ocurre un error en la estructura del documento PDF.
     */
    public byte[] generarPdfHistorial() throws IOException, DocumentException {
        return generarPdf(obtenerHistorial(), "Historial de Accesorios");
    }

    /**
     * Genera un PDF con el historial de accesorios agregados.
     *
     * @return Un array de bytes representando el PDF de accesorios agregados.
     * @throws IOException       Si ocurre un error al generar el archivo.
     * @throws DocumentException Si ocurre un error en la estructura del documento PDF.
     */
    public byte[] generarPdfAgregados() throws IOException, DocumentException {
        List<HistorialAccesorio> agregados = obtenerHistorial().stream()
                .filter(h -> h.getAccion() == Accion.AGREGADO)
                .collect(Collectors.toList());
        return generarPdf(agregados, "Historial de Accesorios Agregados");
    }

    /**
     * Genera un PDF con el historial de accesorios eliminados.
     *
     * @return Un array de bytes representando el PDF de accesorios eliminados.
     * @throws IOException       Si ocurre un error al generar el archivo.
     * @throws DocumentException Si ocurre un error en la estructura del documento PDF.
     */
    public byte[] generarPdfEliminados() throws IOException, DocumentException {
        List<HistorialAccesorio> eliminados = obtenerHistorial().stream()
                .filter(h -> h.getAccion() == Accion.ELIMINADO)
                .collect(Collectors.toList());
        return generarPdf(eliminados, "Historial de Accesorios Eliminados");
    }
}
