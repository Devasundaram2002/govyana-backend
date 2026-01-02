package com.deva.Govyana.service;

import jakarta.mail.internet.MimeMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

@Service
public class EmailService {

    @Autowired
    private JavaMailSender mailSender;

    // 🔥 Must be SAME as MAIL_USERNAME (Gmail)
    private final String FROM_EMAIL = "yourgmail@gmail.com";

    // OTP Mail
    public void sendOtp(String toEmail, String otp) {
        try {
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true);

            helper.setFrom("GoVyana <" + FROM_EMAIL + ">");
            helper.setTo(toEmail);
            helper.setSubject("GoVyana - Password Reset OTP");

            helper.setText(
                    "Your OTP is: " + otp + "\n\n" +
                    "This OTP is valid for 5 minutes.\n\n" +
                    "If you did not request this, please ignore.\n\n" +
                    "— Team GoVyana"
            );

            mailSender.send(message);
            System.out.println("OTP email sent successfully to " + toEmail);

        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("Failed to send OTP email");
        }
    }

    // Welcome Mail
    public void sendWelcomeMail(String toEmail, String fullName) {
        try {
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true);

            helper.setFrom("GoVyana <" + FROM_EMAIL + ">");
            helper.setTo(toEmail);
            helper.setSubject("Welcome to GoVyana ✈️");

            helper.setText(
                    "Hi " + fullName + ",\n\n" +
                    "Welcome to GoVyana 🎉\n\n" +
                    "We’re excited to have you onboard.\n" +
                    "Start exploring flights, packages and amazing journeys with us.\n\n" +
                    "Happy Travelling ✨\n\n" +
                    "Regards,\n" +
                    "Team GoVyana"
            );

            mailSender.send(message);
            System.out.println("Welcome email sent successfully to " + toEmail);

        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("Failed to send Welcome email");
        }
    }
}
