package com.DriveZone.DriveZone.services;
import com.DriveZone.DriveZone.models.Factura;
import com.itextpdf.kernel.pdf.*;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.Paragraph;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import com.DriveZone.DriveZone.models.Factura;
import com.DriveZone.DriveZone.models.OrdenCompra;
import com.DriveZone.DriveZone.repository.FacturaRepository;
import com.DriveZone.DriveZone.repository.OrdenCompraRepository;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.element.Paragraph;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.ByteArrayOutputStream;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
public class FacturaService {
     @Autowired
    private FacturaRepository facturaRepository;

    @Autowired
    private OrdenCompraRepository ordenCompraRepository;

    /*public Factura generarFactura(Factura facturaData) {
        System.out.println("Buscando orden de compra con ID: " + facturaData.getOrdenCompra().getIdOrdenCompra());
        Optional<OrdenCompra> ordenOpt = ordenCompraRepository.findById(facturaData.getOrdenCompra().getIdOrdenCompra());
        if (!ordenOpt.isPresent()) {
            throw new RuntimeException("Orden de compra no encontrada.");
        }

        OrdenCompra orden = ordenOpt.get();
        Factura factura = new Factura();
        factura.setEmpresaNombre(facturaData.getEmpresaNombre());
        factura.setNit(facturaData.getNit());
        factura.setDireccion(facturaData.getDireccion());
        factura.setMetodoPago(facturaData.getMetodoPago());
        //factura.setFecha(LocalDate.now());
        factura.setFecha(Date.from(LocalDate.now().atStartOfDay(ZoneId.systemDefault()).toInstant()));
        factura.setSubtotal(facturaData.getSubtotal());
        factura.setDescuento(facturaData.getDescuento());
        factura.setImpuestos(facturaData.getImpuestos());
        factura.setTotal(facturaData.getTotal());
        factura.setOrdenCompra(orden);

        return facturaRepository.save(factura);
    }*/
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

    public List<Factura> obtenerTodas() {
        return facturaRepository.findAll();
    }

    public Optional<Factura> obtenerPorId(Integer id) {
        return facturaRepository.findById(id);
    }

    public Factura guardarFactura(Factura factura) {
        return facturaRepository.save(factura);
    }

    public void eliminarFactura(Integer id) {
        facturaRepository.deleteById(id);
    }
    public byte[] generarFacturaPDF(int id) {
        Factura factura = facturaRepository.findById(id).orElse(null);
        try (ByteArrayOutputStream baos = new ByteArrayOutputStream()) {
            PdfWriter writer = new PdfWriter(baos);
            PdfDocument pdf = new PdfDocument(writer);
            Document document = new Document(pdf);

            document.add(new Paragraph("Factura N°: " + factura.getIdFactura()));
            document.add(new Paragraph("Cliente: " + factura.getEmpresaNombre()));
            document.add(new Paragraph("Fecha: " + factura.getFecha()));
            document.add(new Paragraph("Total: $" + factura.getTotal()));

            document.close();
            return baos.toByteArray();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}
