//package org.example.service;
//
//import org.springframework.stereotype.Service;
//
//import javax.mail.*;
//import javax.mail.internet.InternetAddress;
//import javax.mail.internet.MimeMessage;
//import java.util.Properties;
//
//@Service
//public class EmailSender {
//    public void send(String message_text) throws  AuthenticationFailedException{
//        final String username = "hello1@gmail.com";
//        final String password = "noPass";
//        String toEmail = "hello2@gmail.com";
//
//        // Set the properties for the email server
//        Properties props = new Properties();
//        props.put("mail.smtp.auth", "true");
//        props.put("mail.smtp.starttls.enable", "true");
//        props.put("mail.smtp.host", "smtp.gmail.com");
//        props.put("mail.smtp.port", "587");
//
//        // Create a Session object
//        Session session = Session.getInstance(props, new Authenticator() {
//            @Override
//            protected PasswordAuthentication getPasswordAuthentication() {
//                return new PasswordAuthentication(username, password);
//            }
//        });
//
//        try {
//            // Create a MimeMessage object
//            Message message = new MimeMessage(session);
//
//            // Set the sender and recipient addresses
//            message.setFrom(new InternetAddress(username));
//            message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(toEmail));
//
//            // Set the email subject and content
//            message.setSubject("Job Result");
//            message.setText(message_text);
//
//            // Send the email
//            Transport.send(message);
//
//            System.out.println("Email sent successfully.");
//
//        } catch (MessagingException e) {
//            e.printStackTrace();
//        }
//    }
//}