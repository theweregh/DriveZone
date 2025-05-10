package com.drivezone.services;

import com.drivezone.models.PriceHistory;
import com.drivezone.repository.PriceHistoryRepository;
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

@Service
public class PriceHistoryService {
    private final PriceHistoryRepository repository;

    public PriceHistoryService(PriceHistoryRepository repository) {
        this.repository = repository;
    }

    public List<PriceHistory> obtenerHistorial(String fechaInicio, String fechaFin) {
        return repository.findByFechaBetween(fechaInicio, fechaFin);
    }

    public byte[] generarPdfHistorial(String fechaInicio, String fechaFin) throws IOException {
        List<PriceHistory> lista = obtenerHistorial(fechaInicio, fechaFin);
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
            Paragraph title = new Paragraph("Informe Histórico de Precios",
                    FontFactory.getFont(FontFactory.HELVETICA_BOLD, 16));
            title.setAlignment(Paragraph.ALIGN_CENTER);
            doc.add(title);
            String hoy = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());
            doc.add(new Paragraph("Fecha: " + hoy));
            doc.add(new Paragraph("\n"));
            PdfPTable table = new PdfPTable(5);
            table.setWidthPercentage(100);
            table.setWidths(new float[]{1, 3, 2, 2, 3});
            table.addCell("ID");
            table.addCell("Producto");
            table.addCell("Precio Anterior");
            table.addCell("Precio Actual");
            table.addCell("Fecha de Modificación");
            lista.forEach(ph -> {
                table.addCell(String.valueOf(ph.getId()));
                table.addCell(ph.getNombreProducto());
                table.addCell(String.format("%.2f", ph.getPrecioAnterior()));
                table.addCell(String.format("%.2f", ph.getPrecioActual()));
                table.addCell(new SimpleDateFormat("yyyy-MM-dd").format(ph.getFechaModificacion()));
            });
            doc.add(table);
            doc.close();
        } catch (DocumentException e) {
            throw new IOException("Error generando PDF", e);
        }
        return out.toByteArray();
    }
}