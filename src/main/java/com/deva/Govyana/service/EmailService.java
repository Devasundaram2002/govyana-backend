package com.deva.Govyana.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class EmailService {

    @Autowired
    private JavaMailSender mailSender;

    // OTP mail (already exists)
    public void sendOtp(String toEmail, String otp) {

        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(toEmail);
        message.setSubject("GoVyana - Password Reset OTP");
        message.setText(
            "Your OTP is: " + otp + "\n\n" +
            "This OTP is valid for 5 minutes.\n\n" +
            "If you did not request this, please ignore."
        );

        mailSender.send(message);
    }

    // ✅ NEW: Welcome Mail
    public void sendWelcomeMail(String toEmail, String fullName) {

        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(toEmail);
        message.setSubject("Welcome to GoVyana ✈️");

        message.setText(
            "Hi " + fullName + ",\n\n" +
            "Welcome to GoVyana 🎉\n\n" +
            "We’re excited to have you onboard.\n" +
            "Start exploring flights, packages and amazing journeys with us.\n\n" +
            "Happy Travelling ✨\n\n" +
            "Regards,\n" +
            "Team GoVyana"
        );

        mailSender.send(message);
    }
}
