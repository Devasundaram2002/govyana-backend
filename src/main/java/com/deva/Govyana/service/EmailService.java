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

    private final String FROM_EMAIL = "onboarding@resend.dev";

    // OTP mail
    public void sendOtp(String toEmail, String otp) {
        try {
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true);

            helper.setFrom(FROM_EMAIL);
            helper.setTo(toEmail);
            helper.setSubject("GoVyana - Password Reset OTP");
            helper.setText(
                    "Your OTP is: " + otp + "\n\n" +
                    "This OTP is valid for 5 minutes.\n\n" +
                    "If you did not request this, please ignore."
            );

            mailSender.send(message);
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

            helper.setFrom(FROM_EMAIL);
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
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("Failed to send Welcome email");
        }
    }
}
