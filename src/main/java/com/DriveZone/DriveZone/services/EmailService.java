package com.DriveZone.DriveZone.services;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import java.nio.charset.StandardCharsets;

/**
 * Servicio para el envío de correos electrónicos.
 * <p>
 * Permite enviar facturas en formato PDF adjunto a los clientes.
 * </p>
 *
 * @author DriveZone Team
 * @version 1.1
 * @since 2025-03-30
 */
@Service
public class EmailService {
    /**
     * Dirección de correo electrónico utilizada como remitente.
     */
    private static final String REMITENTE = "zone.drive@yandex.com"; // Tu correo de Yandex
    @Autowired
    private JavaMailSender mailSender;

    /**
     * Envía un correo con una factura adjunta en formato PDF.
     *
     * @param destinatario  Dirección de correo del receptor.
     * @param pdfAdjunto    Archivo PDF en formato de bytes.
     * @param nombreFactura Nombre del archivo adjunto.
     * @throws MessagingException Si ocurre un error en el envío del correo.
     */
    public void enviarFacturaPorCorreo(String destinatario, byte[] pdfAdjunto, String nombreFactura) throws MessagingException {
        // Crear el mensaje
        MimeMessage mensaje = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(mensaje, true, StandardCharsets.UTF_8.name());

        // Configurar detalles del correo
        helper.setFrom(REMITENTE);
        helper.setTo(destinatario);
        helper.setSubject("Factura de tu compra");
        helper.setText("Adjunto encontrarás la factura de tu compra. ¡Gracias por tu preferencia!");

        // Adjuntar el PDF
        helper.addAttachment(nombreFactura, new ByteArrayResource(pdfAdjunto));

        // Enviar el correo
        mailSender.send(mensaje);
        System.out.println("✅ Correo enviado exitosamente a " + destinatario);
    }
    public void enviarRespuestaGarantia(String destinatario, String estado, String mensajeAdicional) throws MessagingException {
    MimeMessage mensaje = mailSender.createMimeMessage();
    MimeMessageHelper helper = new MimeMessageHelper(mensaje, true, StandardCharsets.UTF_8.name());

    helper.setFrom(REMITENTE);
    helper.setTo(destinatario);
    helper.setSubject("Respuesta a tu solicitud de garantía");

    String contenido = String.format("""
        Estimado cliente,<br><br>
        Su solicitud de garantía ha sido procesada. El estado actualizado es: <strong>%s</strong>.<br><br>
        %s<br><br>
        Gracias por confiar en DriveZone.<br><br>
        --<br>
        DriveZone Team
    """, estado.toUpperCase(), mensajeAdicional != null ? mensajeAdicional : "");

    helper.setText(contenido, true);
    mailSender.send(mensaje);
    System.out.println("📧 Correo de garantía enviado a: " + destinatario);
}

}


