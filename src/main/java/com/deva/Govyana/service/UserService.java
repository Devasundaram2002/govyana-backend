package com.deva.Govyana.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.deva.Govyana.dto.LoginRequest;
import com.deva.Govyana.model.User;
import com.deva.Govyana.repository.UserRepository;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private EmailService emailService;
  

    // REGISTER
    public String register(User user) {

        // 1️⃣ Email already exists check
        if (userRepository.findByEmail(user.getEmail()).isPresent()) {
            return "Email already registered";
        }

        // 2️⃣ Encrypt password
        user.setPassword(passwordEncoder.encode(user.getPassword()));

        // 3️⃣ Clear OTP fields (register ku thevai illa)
        user.setOtp(null);
        user.setOtpExpiry(null);

        // 4️⃣ Save user to DB
        User savedUser = userRepository.save(user);

        // 5️⃣ Send Welcome Mail
        emailService.sendWelcomeMail(
                savedUser.getEmail(),
                savedUser.getFullName()
        );

        return "User registered successfully";
    }

    // LOGIN
    @Autowired
    private PasswordEncoder passwordEncoder;

    public String login(LoginRequest req) {

        User user = userRepository.findByEmail(req.getEmail())
            .orElseThrow(() -> new RuntimeException("User not found"));

        if (!passwordEncoder.matches(req.getPassword(), user.getPassword())) {
            throw new RuntimeException("Invalid email or password");
        }

        return "Login success";
    }


}
