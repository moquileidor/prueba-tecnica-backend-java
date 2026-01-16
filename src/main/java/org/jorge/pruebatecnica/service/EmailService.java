package org.jorge.pruebatecnica.service;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class EmailService {

    private final JavaMailSender mailSender;

    @Value("${spring.mail.username}")
    private String fromEmail;

    @Value("${app.frontend.url}")
    private String frontendUrl;

    public void sendRegistrationEmail(String to, String fullName, String token) {
        String subject = "Bienvenido - Configura tu contraseña";
        String link = frontendUrl + "/set-password.html?token=" + token;
        
        String htmlContent = """
            <html>
            <body style="font-family: Arial, sans-serif;">
                <h2>¡Bienvenido %s!</h2>
                <p>Gracias por registrarte en nuestro sistema.</p>
                <p>Para completar tu registro, por favor configura tu contraseña haciendo clic en el siguiente enlace:</p>
                <p><a href="%s" style="background-color: #4CAF50; color: white; padding: 10px 20px; text-decoration: none; border-radius: 5px;">Configurar Contraseña</a></p>
                <p>Este enlace es válido por 24 horas y solo puede usarse una vez.</p>
                <p>Si no solicitaste este registro, por favor ignora este correo.</p>
                <br>
                <p>Saludos cordiales,</p>
                <p>El equipo</p>
            </body>
            </html>
            """.formatted(fullName, link);

        sendEmail(to, subject, htmlContent);
    }

    public void sendPasswordResetEmail(String to, String fullName, String token) {
        String subject = "Recuperación de Contraseña";
        String link = frontendUrl + "/reset.html?token=" + token;
        
        String htmlContent = """
            <html>
            <body style="font-family: Arial, sans-serif;">
                <h2>Hola %s,</h2>
                <p>Recibimos una solicitud para restablecer tu contraseña.</p>
                <p>Para crear una nueva contraseña, haz clic en el siguiente enlace:</p>
                <p><a href="%s" style="background-color: #2196F3; color: white; padding: 10px 20px; text-decoration: none; border-radius: 5px;">Restablecer Contraseña</a></p>
                <p>Este enlace es válido por 1 hora y solo puede usarse una vez.</p>
                <p>Si no solicitaste este cambio, por favor ignora este correo y tu contraseña permanecerá sin cambios.</p>
                <br>
                <p>Saludos cordiales,</p>
                <p>El equipo</p>
            </body>
            </html>
            """.formatted(fullName, link);

        sendEmail(to, subject, htmlContent);
    }

    private void sendEmail(String to, String subject, String htmlContent) {
        try {
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");
            
            helper.setFrom(fromEmail);
            helper.setTo(to);
            helper.setSubject(subject);
            helper.setText(htmlContent, true);
            
            mailSender.send(message);
        } catch (MessagingException e) {
            throw new RuntimeException("Error al enviar el correo electrónico", e);
        }
    }
}
