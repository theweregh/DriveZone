package com.drivezone.services;

import com.drivezone.models.Evento;
import com.drivezone.repository.EventRepository;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.FontFactory;
import com.itextpdf.text.Image;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import org.springframework.stereotype.Service;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class EventService {
    private final EventRepository repository;

    public EventService(EventRepository repository) {
        this.repository = repository;
    }

    public List<Evento> obtenerEventos(String fechaInicio, String fechaFin) {
        return repository.findByFechaBetween(fechaInicio, fechaFin);
    }

    public byte[] generarPdfEventos(String fechaInicio, String fechaFin) throws IOException {
        List<Evento> lista = obtenerEventos(fechaInicio, fechaFin);
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        Document doc = new Document();
        try {
            PdfWriter.getInstance(doc, out);
            doc.open();
            try {
                Image logo = Image.getInstance("src/main/resources/static/img/DriveZone.png");
                logo.scaleToFit(80, 80);
                doc.add(logo);
            } catch (Exception e) {}
            Paragraph title = new Paragraph("Control de Eventos",
                    FontFactory.getFont(FontFactory.HELVETICA_BOLD, 16));
            title.setAlignment(Paragraph.ALIGN_CENTER);
            doc.add(title);
            String hoy = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());
            doc.add(new Paragraph("Fecha Reporte: " + hoy));
            doc.add(new Paragraph("\n"));
            PdfPTable table = new PdfPTable(4);
            table.setWidthPercentage(100);
            table.setWidths(new float[]{1, 2, 3, 4});
            table.addCell("ID");
            table.addCell("Tipo");
            table.addCell("Fecha");
            table.addCell("Descripción");
            lista.forEach(ev -> {
                table.addCell(String.valueOf(ev.getId()));
                table.addCell(ev.getTipo().name());
                table.addCell(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(ev.getFecha()));
                table.addCell(ev.getDescripcion());
            });
            doc.add(table);
            doc.close();
        } catch (DocumentException e) {
            throw new IOException("Error generando PDF", e);
        }
        return out.toByteArray();
    }
}