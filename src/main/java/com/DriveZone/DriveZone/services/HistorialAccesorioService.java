package com.DriveZone.DriveZone.services;

import com.DriveZone.DriveZone.models.HistorialAccesorio;
import com.DriveZone.DriveZone.repository.HistorialAccesorioRepository;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Element;
import com.itextpdf.text.Font;
import com.itextpdf.text.Image;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.Rectangle;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import com.itextpdf.text.pdf.parser.clipper.Paths;
import org.springframework.stereotype.Service;
import com.itextpdf.text.Document;


import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.List;

@Service
public class HistorialAccesorioService {
    private final HistorialAccesorioRepository historialAccesorioRepository;

    public HistorialAccesorioService(HistorialAccesorioRepository historialAccesorioRepository) {
        this.historialAccesorioRepository = historialAccesorioRepository;
    }

    public List<HistorialAccesorio> obtenerHistorial() {
        return historialAccesorioRepository.findAll();
    }

    public byte[] generarPdfHistorial() throws IOException, DocumentException {
        List<HistorialAccesorio> historial = obtenerHistorial();

        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        Document document = new Document();
        PdfWriter.getInstance(document, outputStream);
        document.open();

        document.add(new Paragraph("Historial de Accesorios"));
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
}
