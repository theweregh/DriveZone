package com.DriveZone.DriveZone.services;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import java.nio.charset.StandardCharsets;

@Service
public class EmailService {

    @Autowired
    private JavaMailSender mailSender;

    private static final String REMITENTE = "zone.drive@yandex.com"; // Tu correo de Yandex

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
}


