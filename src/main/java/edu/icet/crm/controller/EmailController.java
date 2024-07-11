package edu.icet.crm.controller;

import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;
import java.util.Properties;

public class EmailController {
    private static final String EMAIL_USERNAME = "dhanuthfernando2001@gmail.com";
    private static final String EMAIL_PASSWORD = "xnpw urth zzbg hwhd";
    private static final String SMTP_HOST = "smtp.gmail.com";
    private static final int SMTP_PORT = 587; // Adjust as per your SMTP configuration

    public boolean sendEmail(String recipientEmail, String subject, String body) {
        Properties props = new Properties();
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.starttls.enable", "true");
        props.put("mail.smtp.host", SMTP_HOST);
        props.put("mail.smtp.port", SMTP_PORT);

        Session session = Session.getInstance(props, new Authenticator() {
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(EMAIL_USERNAME, EMAIL_PASSWORD);
            }
        });

        // Enable debugging to see the SMTP protocol messages
        session.setDebug(true);

        try {
            Message message = new MimeMessage(session);
            message.setFrom(new InternetAddress(EMAIL_USERNAME));
            message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(recipientEmail));
            message.setSubject(subject);
            message.setText(body);

            Transport.send(message);
            return true; // Email sent successfully
        } catch (MessagingException e) {
            e.printStackTrace();
            return false; // Failed to send email
        }
    }

    public boolean sendEmailWithAttachment(String recipientEmail, String subject, byte[] attachment) {
        Properties props = new Properties();
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.starttls.enable", "true");
        props.put("mail.smtp.host", SMTP_HOST);
        props.put("mail.smtp.port", SMTP_PORT);

        Session session = Session.getInstance(props, new Authenticator() {
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(EMAIL_USERNAME, EMAIL_PASSWORD);
            }
        });

        // Enable debugging to see the SMTP protocol messages
        session.setDebug(true);

        try {
            Message message = new MimeMessage(session);
            message.setFrom(new InternetAddress(EMAIL_USERNAME));
            message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(recipientEmail));
            message.setSubject(subject);

            // Create multipart message
            Multipart multipart = new MimeMultipart();


            MimeBodyPart attachmentBodyPart = new MimeBodyPart();
            attachmentBodyPart.setContent(attachment, "application/pdf");
            attachmentBodyPart.setFileName("Confirmation.pdf");
            multipart.addBodyPart(attachmentBodyPart);

            // Set content
            message.setContent(multipart);

            Transport.send(message);
            return true; // Email sent successfully
        } catch (MessagingException e) {
            e.printStackTrace();
            return false; // Failed to send email
        }
    }
}
