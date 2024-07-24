package com.ecobank.soole.services;

import org.springframework.stereotype.Service;

import com.ecobank.soole.util.email.EmailDetails;
import com.ecobank.soole.util.email.EmailDetailsWelcome;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;

import org.springframework.beans.factory.annotation.Value;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;

@Service
public class EmailService {
    
    @Autowired
    private JavaMailSender javaMailSender;

    @Value("${spring.mail.username}")
    private String sender;

    public Boolean sendMail(EmailDetails details){
        try {
            SimpleMailMessage mailMessage = new SimpleMailMessage();
            mailMessage.setFrom(sender);
            mailMessage.setTo(details.getRecipient());
            mailMessage.setText(details.getBody());
            mailMessage.setSubject(details.getSubject());

            javaMailSender.send(mailMessage);
            return true;
        } catch (Exception e) {
            return false;
        }
    }
// String to, String firstName, String verificationLink
    public boolean sendWelcomeEmail(EmailDetailsWelcome details) throws MessagingException {
        try {
            String subject = "Welcome to Soole Bus App";
        String content = buildEmailContent(details.getRecipient(),details.getFirstName(), details.getBody());

        MimeMessage message = javaMailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message, true);

        helper.setTo(details.getRecipient());
        helper.setSubject(subject);
        helper.setText(content, true);

        javaMailSender.send(message);
        return true;
        } catch (Exception e) {
            return false;
        }
        
    }

    private String buildEmailContent(String recipient, String firstName, String message) {
        return "<!DOCTYPE html>"
                + "<html lang=\"en\">"
                + "<head>"
                + "<meta charset=\"UTF-8\">"
                + "<meta name=\"viewport\" content=\"width=device-width, initial-scale=1.0\">"
                + "<title>Welcome to Soole Booking App</title>"
                + "<style>"
                + "body {"
                + "    font-family: Arial, sans-serif;"
                + "    background-color: #f4f4f4;"
                + "    margin: 0;"
                + "    padding: 0;"
                + "}"
                + ".header, .footer {"
                + "    background-color: #0033cc;"
                + "    color: white;"
                + "    text-align: center;"
                + "    padding: 10px 0;"
                + "}"
                + ".content {"
                + "    background-color: white;"
                + "    padding: 20px;"
                + "    margin: 20px auto;"
                + "    width: 80%;"
                + "    max-width: 600px;"
                + "    border-radius: 8px;"
                + "    box-shadow: 0 0 10px rgba(0, 0, 0, 0.1);"
                + "}"
                + ".button {"
                + "    display: inline-block;"
                + "    background-color: #0033cc;"
                + "    color: white;"
                + "    padding: 10px 20px;"
                + "    text-decoration: none;"
                + "    border-radius: 5px;"
                + "    margin-top: 20px;"
                + "}"
                + "</style>"
                + "</head>"
                + "<body>"
                + "    <div class=\"header\">"
                + "        <h1>Welcome to Soole Bus App</h1>"
                + "    </div>"
                + "    <div class=\"content\">"
                + "        <h2>Hi " + firstName + ",</h2>"
                + "        <p>Thank you for joining Soole Bus App! We are thrilled to have you on board.</p>"
                + "        <p>With Soole, you can easily book bus tickets, manage your trips, and enjoy a hassle-free travel experience.</p>"
                + "        <p>You can visit our app on:</p>"
                + "        <a href=\"http://localhost:8080/swagger-ui/index.html" + message + "\" class=\"button\">Visit Soole</a>"
                + "        <p>If you have any questions or need assistance, feel free to reach out to our support team.</p>"
                + "        <p>Happy travels!</p>"
                + "        <p>Best regards,</p>"
                + "        <p>The Soole App Team</p>"
                + "    </div>"
                + "    <div class=\"footer\">"
                + "        <p>&copy; 2024 Soole Bus App Ecobank. All rights reserved.</p>"
                + "        <p>27B, EPAC, Ozumba Mbadiwe, Victoria Island, Lagos.</p>"
                + "        <p><a href=\"ecobank.com/soole\" style=\"color: white;\">Unsubscribe</a></p>"
                + "    </div>"
                + "</body>"
                + "</html>";
    }
}
