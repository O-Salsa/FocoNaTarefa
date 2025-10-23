package com.heilsalsa.foconatarefa.controller;

import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import com.heilsalsa.foconatarefa.controller.dto.LoginRequest;
import com.heilsalsa.foconatarefa.security.JwtUtil;
import com.heilsalsa.foconatarefa.repository.UserRepository;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/auth")
public class AuthController {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public AuthController(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@Valid @RequestBody LoginRequest loginData) {
        return userRepository.findByUsername(loginData.usuario())
                .filter(user -> passwordEncoder.matches(loginData.senha(), user.getPassword()))
                .map(user -> ResponseEntity.ok(Map.of("token", JwtUtil.generateToken(user.getUsername()))))
                .orElseGet(() -> ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                        .body(Map.of("erro", "Usuário ou senha inválidos")));
    }
}
