package com.deva.Govyana.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class EmailService {

    @Autowired
    private JavaMailSender mailSender;

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
}

