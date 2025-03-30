package com.DriveZone.DriveZone.services;

import com.DriveZone.DriveZone.models.Factura;
import com.DriveZone.DriveZone.models.OrdenCompra;
import com.DriveZone.DriveZone.repository.FacturaRepository;
import com.DriveZone.DriveZone.repository.OrdenCompraRepository;
import com.itextpdf.text.*;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import io.jsonwebtoken.io.IOException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.ByteArrayOutputStream;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;
import java.util.List;
import java.util.Optional;

/**
 * Servicio para la gestión de facturas en el sistema.
 *
 * @author DriveZone Team
 * @version 1.1
 * @since 2025-03-30
 */
@Service
public class FacturaService {
    @Autowired
    private FacturaRepository facturaRepository;

    @Autowired
    private OrdenCompraRepository ordenCompraRepository;

    /**
     * Genera una nueva factura basada en los datos proporcionados y la orden de compra asociada.
     *
     * @param facturaData   Datos de la factura a generar.
     * @param idOrdenCompra ID de la orden de compra asociada.
     * @return La factura generada y guardada en la base de datos.
     * @throws RuntimeException Si la orden de compra no es encontrada.
     */
    public Factura generarFactura(Factura facturaData, int idOrdenCompra) {
        System.out.println("Buscando orden de compra con ID: " + idOrdenCompra);

        Optional<OrdenCompra> ordenOpt = ordenCompraRepository.findById(idOrdenCompra);
        if (!ordenOpt.isPresent()) {
            throw new RuntimeException("Orden de compra no encontrada.");
        }

        OrdenCompra orden = ordenOpt.get();
        Factura factura = new Factura();
        factura.setEmpresaNombre(facturaData.getEmpresaNombre());
        factura.setNit(facturaData.getNit());
        factura.setDireccion(facturaData.getDireccion());
        factura.setMetodoPago(facturaData.getMetodoPago());
        factura.setFecha(Date.from(LocalDate.now().atStartOfDay(ZoneId.systemDefault()).toInstant()));
        factura.setSubtotal(facturaData.getSubtotal());
        factura.setDescuento(facturaData.getDescuento());
        factura.setImpuestos(facturaData.getImpuestos());
        factura.setTotal(facturaData.getTotal());
        factura.setOrdenCompra(orden);

        return facturaRepository.save(factura);
    }

    /**
     * Obtiene todas las facturas almacenadas en el sistema.
     *
     * @return Lista de todas las facturas.
     */
    public List<Factura> obtenerTodas() {
        return facturaRepository.findAll();
    }

    /**
     * Busca una factura por su ID.
     *
     * @param id ID de la factura a buscar.
     * @return Un Optional con la factura si se encuentra, o vacío si no existe.
     */
    public Optional<Factura> obtenerPorId(Integer id) {
        return facturaRepository.findById(id);
    }

    /**
     * Guarda una factura en la base de datos.
     *
     * @param factura Factura a guardar.
     * @return La factura guardada.
     */
    public Factura guardarFactura(Factura factura) {
        return facturaRepository.save(factura);
    }

    /**
     * Elimina una factura por su ID.
     *
     * @param id ID de la factura a eliminar.
     */
    public void eliminarFactura(Integer id) {
        facturaRepository.deleteById(id);
    }

    /**
     * Genera un archivo PDF con la información de una factura específica.
     *
     * @param id ID de la factura a generar en PDF.
     * @return Un array de bytes que representa el archivo PDF.
     * @throws IOException         Si ocurre un error en la generación del PDF.
     * @throws java.io.IOException Si hay un problema de entrada/salida.
     * @throws RuntimeException    Si la factura no es encontrada.
     */
    public byte[] generarFacturaPDF(int id) throws IOException, java.io.IOException {
        Factura factura = facturaRepository.findById(id).orElse(null);
        if (factura == null) {
            throw new RuntimeException("Factura no encontrada");
        }

        try (ByteArrayOutputStream baos = new ByteArrayOutputStream()) {
            // Definir el documento y establecer márgenes
            Document document = new Document(PageSize.A4, 50, 50, 50, 50);
            PdfWriter.getInstance(document, baos);
            document.open();

            // Definir colores
            BaseColor pastelBlue = new BaseColor(173, 216, 230); // Azul pastel para encabezados
            BaseColor darkerBlue = new BaseColor(100, 149, 237);   // Azul para el total

            // Fuentes personalizadas
            Font titleFont = new Font(Font.FontFamily.HELVETICA, 20, Font.BOLD, new BaseColor(0, 102, 204));
            Font headerFont = new Font(Font.FontFamily.HELVETICA, 12, Font.BOLD, BaseColor.WHITE);
            Font normalFont = new Font(Font.FontFamily.HELVETICA, 10);  // Por defecto, texto en negro
            Font totalFont = new Font(Font.FontFamily.HELVETICA, 14, Font.BOLD, BaseColor.WHITE);

            // Agregar logo a la izquierda
            try {
                Image logo = Image.getInstance("src/main/resources/static/img/DriveZone.png");

                logo.scaleToFit(100, 100);
                logo.setAlignment(Element.ALIGN_LEFT);
                document.add(logo);
            } catch (Exception e) {
                System.out.println("Logo no encontrado o error al cargar la imagen.");
            }

            // Título principal
            Paragraph title = new Paragraph("FACTURA DE COMPRA", titleFont);
            title.setAlignment(Element.ALIGN_CENTER);
            document.add(title);
            document.add(new Paragraph("DriveZone\n\n", normalFont));

            // Tabla de información de la factura
            PdfPTable infoTable = new PdfPTable(2);
            infoTable.setWidthPercentage(100);
            infoTable.setWidths(new float[]{40, 60});
            addTableRow(infoTable, "Factura N°:", String.valueOf(factura.getIdFactura()), headerFont, pastelBlue, normalFont, BaseColor.WHITE);
            addTableRow(infoTable, "Cliente:", factura.getEmpresaNombre(), headerFont, pastelBlue, normalFont, BaseColor.WHITE);
            addTableRow(infoTable, "Fecha:", factura.getFecha().toString(), headerFont, pastelBlue, normalFont, BaseColor.WHITE);
            addTableRow(infoTable, "Dirección:", factura.getDireccion(), headerFont, pastelBlue, normalFont, BaseColor.WHITE);
            addTableRow(infoTable, "Método de Pago:", factura.getMetodoPago(), headerFont, pastelBlue, normalFont, BaseColor.WHITE);
            document.add(infoTable);
            document.add(new Paragraph("\n"));

            // Tabla de totales
            PdfPTable totalTable = new PdfPTable(2);
            totalTable.setWidthPercentage(100);
            totalTable.setWidths(new float[]{75, 25});
            // Para Subtotal, Descuento e Impuestos: etiqueta en blanco y valor en negro
            addTableRow(totalTable, "Subtotal:", "$" + String.format("%.2f", factura.getSubtotal()), headerFont, pastelBlue, normalFont, BaseColor.WHITE);
            addTableRow(totalTable, "Descuento:", "$" + String.format("%.2f", factura.getDescuento()), headerFont, pastelBlue, normalFont, BaseColor.WHITE);
            addTableRow(totalTable, "Impuestos:", "$" + String.format("%.2f", factura.getImpuestos()), headerFont, pastelBlue, normalFont, BaseColor.WHITE);
            // Para TOTAL: etiqueta en blanco y valor en blanco con fondo azul oscuro
            addTableRow(totalTable, "TOTAL:", "$" + String.format("%.2f", factura.getTotal()), headerFont, darkerBlue, totalFont, darkerBlue);
            document.add(totalTable);

            // Pie de página opcional
            Paragraph footer = new Paragraph("\n¡Gracias por su compra!", normalFont);
            footer.setAlignment(Element.ALIGN_CENTER);
            document.add(footer);

            document.close();
            return baos.toByteArray();
        } catch (DocumentException e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * Agrega una fila a una tabla con el mismo estilo para ambas celdas.
     *
     * @param table           Tabla a la que se añadirá la fila.
     * @param label           Texto de la celda de etiqueta.
     * @param value           Texto de la celda de valor.
     * @param font            Fuente utilizada para ambas celdas.
     * @param backgroundColor Color de fondo de la celda de etiqueta.
     */
    private void addTableRow(PdfPTable table, String label, String value, Font font, BaseColor backgroundColor) {
        PdfPCell cellLabel = new PdfPCell(new Phrase(label, font));
        cellLabel.setBackgroundColor(backgroundColor);
        cellLabel.setHorizontalAlignment(Element.ALIGN_LEFT);
        cellLabel.setPadding(8);
        cellLabel.setBorderColor(BaseColor.LIGHT_GRAY);
        cellLabel.setBorderWidth(1);
        table.addCell(cellLabel);

        PdfPCell cellValue = new PdfPCell(new Phrase(value, font));
        cellValue.setBackgroundColor(BaseColor.WHITE);
        cellValue.setHorizontalAlignment(Element.ALIGN_LEFT);
        cellValue.setPadding(8);
        cellValue.setBorderColor(BaseColor.LIGHT_GRAY);
        cellValue.setBorderWidth(1);
        table.addCell(cellValue);
    }

    /**
     * Agrega una fila a una tabla con diferentes fuentes y colores para cada celda.
     *
     * @param table     Tabla a la que se añadirá la fila.
     * @param label     Texto de la celda de etiqueta.
     * @param value     Texto de la celda de valor.
     * @param labelFont Fuente de la celda de etiqueta.
     * @param labelBg   Color de fondo de la celda de etiqueta.
     * @param valueFont Fuente de la celda de valor.
     * @param valueBg   Color de fondo de la celda de valor.
     */
    private void addTableRow(PdfPTable table, String label, String value, Font labelFont, BaseColor labelBg, Font valueFont, BaseColor valueBg) {
        PdfPCell cellLabel = new PdfPCell(new Phrase(label, labelFont));
        cellLabel.setBackgroundColor(labelBg);
        cellLabel.setHorizontalAlignment(Element.ALIGN_LEFT);
        cellLabel.setPadding(8);
        cellLabel.setBorderColor(BaseColor.LIGHT_GRAY);
        cellLabel.setBorderWidth(1);
        table.addCell(cellLabel);

        PdfPCell cellValue = new PdfPCell(new Phrase(value, valueFont));
        cellValue.setBackgroundColor(valueBg);
        cellValue.setHorizontalAlignment(Element.ALIGN_LEFT);
        cellValue.setPadding(8);
        cellValue.setBorderColor(BaseColor.LIGHT_GRAY);
        cellValue.setBorderWidth(1);
        table.addCell(cellValue);
    }
}

