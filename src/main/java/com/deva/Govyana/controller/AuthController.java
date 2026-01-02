package com.deva.Govyana.controller;

import java.time.LocalDateTime;
import java.util.Map;
import java.util.Optional;
import java.util.Random;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import com.deva.Govyana.dto.LoginRequest;
import com.deva.Govyana.dto.ResetPasswordRequest;
import com.deva.Govyana.dto.VerifyOtpRequest;
import com.deva.Govyana.model.User;
import com.deva.Govyana.repository.UserRepository;
import com.deva.Govyana.service.EmailService;
import com.deva.Govyana.service.UserService;

@RestController
@RequestMapping("/auth")
@CrossOrigin(origins = "*")
public class AuthController {

    @Autowired
    private UserService userService;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private EmailService emailService;

    @Autowired
    private PasswordEncoder passwordEncoder;

    // ================= REGISTER =================
    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody User user) {
        try {
            userService.register(user);
            return ResponseEntity.ok("Registered successfully");
        } catch (RuntimeException e) {
            if (e.getMessage().equals("EMAIL_EXISTS")) {
                return ResponseEntity.badRequest().body("Email already exists");
            }
            return ResponseEntity.internalServerError().body("Server error");
        }
    }

    // ================= LOGIN =================
    @PostMapping("/login")
    public ResponseEntity<String> login(@RequestBody LoginRequest request) {
        try {
            userService.login(request);
            return ResponseEntity.ok("Login success");
        } catch (RuntimeException e) {

            if (e.getMessage().equals("USER_NOT_FOUND")) {
                return ResponseEntity.status(404).body("User not registered");
            }

            if (e.getMessage().equals("INVALID_PASSWORD")) {
                return ResponseEntity.status(401).body("Invalid email or password");
            }

            return ResponseEntity.internalServerError().body("Server error");
        }
    }

    // ================= FORGOT PASSWORD =================
    @PostMapping("/forgot-password")
    public ResponseEntity<?> forgotPassword(@RequestBody Map<String, String> req) {

        String email = req.get("email");

        Optional<User> userOpt = userRepository.findByEmail(email);

        if (userOpt.isPresent()) {
            User user = userOpt.get();

            String otp = String.valueOf(new Random().nextInt(900000) + 100000);

            user.setOtp(otp);
            user.setOtpExpiry(LocalDateTime.now().plusMinutes(5));
            userRepository.save(user);

            // Send OTP Mail
            emailService.sendOtp(email, otp);
        }

        // Security response (same message for all)
        return ResponseEntity.ok(
                Map.of("message", "If this email exists, OTP has been sent")
        );
    }

    // ================= VERIFY OTP =================
    @PostMapping("/verify-otp")
    public ResponseEntity<?> verifyOtp(@RequestBody VerifyOtpRequest req) {

        User user = userRepository.findByEmail(req.getEmail())
                .orElseThrow(() -> new RuntimeException("User not found"));

        if (user.getOtp() == null ||
            user.getOtpExpiry().isBefore(LocalDateTime.now())) {
            return ResponseEntity.badRequest().body("OTP expired");
        }

        if (!user.getOtp().equals(req.getOtp())) {
            return ResponseEntity.badRequest().body("Invalid OTP");
        }

        // Clear OTP after success
        user.setOtp(null);
        user.setOtpExpiry(null);
        userRepository.save(user);

        return ResponseEntity.ok("OTP verified successfully");
    }

    // ================= RESET PASSWORD =================
    @PostMapping("/reset-password")
    public ResponseEntity<String> resetPassword(
            @RequestBody ResetPasswordRequest request) {

        User user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new RuntimeException("User not found"));

        user.setPassword(passwordEncoder.encode(request.getNewPassword()));
        userRepository.save(user);

        return ResponseEntity.ok("Password reset successful");
    }
}
