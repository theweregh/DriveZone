package com.DriveZone.DriveZone;

import com.DriveZone.DriveZone.services.EmailService;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mail.javamail.JavaMailSender;

import java.nio.charset.StandardCharsets;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class EmailServiceTest {
    @Mock
    private JavaMailSender mailSender;

    @InjectMocks
    private EmailService emailService;

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

}
