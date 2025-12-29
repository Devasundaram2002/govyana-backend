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

  

    // REGISTER
    public String register(User user) {

        if (userRepository.findByEmail(user.getEmail()).isPresent()) {
            return "EMAIL_EXISTS";
        }

        user.setPassword(passwordEncoder.encode(user.getPassword()));
        userRepository.save(user);
        return "SUCCESS";
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
