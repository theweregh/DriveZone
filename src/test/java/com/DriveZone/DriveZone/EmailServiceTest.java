package com.DriveZone.DriveZone;

import com.DriveZone.DriveZone.services.EmailService;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mail.javamail.JavaMailSender;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class EmailServiceTest {

    @Mock
    private JavaMailSender mailSender;

    @InjectMocks
    private EmailService emailService;

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

}
