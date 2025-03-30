package com.DriveZone.DriveZone;

import com.DriveZone.DriveZone.services.EmailService;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
<<<<<<< HEAD
import org.junit.jupiter.api.BeforeEach;
=======
>>>>>>> d39b72f173105884b0704cdc2cfeeed6b28d906b
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
<<<<<<< HEAD
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mail.javamail.JavaMailSender;

import java.nio.charset.StandardCharsets;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class EmailServiceTest {
=======
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mail.javamail.JavaMailSender;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyBoolean;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import org.springframework.mail.javamail.MimeMessageHelper;

@ExtendWith(MockitoExtension.class)
class EmailServiceTest {

>>>>>>> d39b72f173105884b0704cdc2cfeeed6b28d906b
    @Mock
    private JavaMailSender mailSender;

    @InjectMocks
    private EmailService emailService;

<<<<<<< HEAD
    private MimeMessage mimeMessage;

    @BeforeEach
    void setUp() {
        mimeMessage = mock(MimeMessage.class);
        when(mailSender.createMimeMessage()).thenReturn(mimeMessage);
    }

    @Test
    void testEnviarFacturaPorCorreo() throws MessagingException {
        String destinatario = "cliente@ejemplo.com";
        byte[] pdfAdjunto = "Factura en PDF".getBytes(StandardCharsets.UTF_8);
        String nombreFactura = "factura.pdf";

        emailService.enviarFacturaPorCorreo(destinatario, pdfAdjunto, nombreFactura);

        verify(mailSender, times(1)).send(any(MimeMessage.class));
    }

    @Test
    void testEnviarFacturaPorCorreo_MensajeException() {
        String destinatario = "cliente@ejemplo.com";
        byte[] pdfAdjunto = "Factura en PDF".getBytes(StandardCharsets.UTF_8);
        String nombreFactura = "factura.pdf";

        doThrow(new RuntimeException("Error al enviar correo")).when(mailSender).send(any(MimeMessage.class));

        assertThrows(RuntimeException.class, () ->
                emailService.enviarFacturaPorCorreo(destinatario, pdfAdjunto, nombreFactura));

        verify(mailSender, times(1)).send(any(MimeMessage.class));
    }
=======
    @Test
    void testEnviarFacturaPorCorreo_EnvioExitoso() throws MessagingException {
        // Datos de prueba
        String destinatario = "cliente@example.com";
        byte[] pdfAdjunto = new byte[10];
        String nombreFactura = "factura.pdf";

        // Mock de MimeMessage
        MimeMessage mimeMessage = mock(MimeMessage.class);
        when(mailSender.createMimeMessage()).thenReturn(mimeMessage);

        // Ejecutar el método y verificar que no lanza excepciones
        assertDoesNotThrow(() -> emailService.enviarFacturaPorCorreo(destinatario, pdfAdjunto, nombreFactura));

        // Verificar que JavaMailSender intentó enviar un correo
        verify(mailSender, times(1)).send(mimeMessage);
    }

>>>>>>> d39b72f173105884b0704cdc2cfeeed6b28d906b

}
