package com.example.demo.controller.api;

import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.dto.LoginRequest;
import com.example.demo.dto.LoginResponse;
import com.example.demo.entity.User;
import com.example.demo.repository.UserRepository;
import com.example.demo.security.JwtService;

@RestController
@RequestMapping("/api/auth")
public class AuthRestController {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;

    public AuthRestController(UserRepository userRepository,
                              PasswordEncoder passwordEncoder,
                              JwtService jwtService) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtService = jwtService;
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest request) {

        User user = userRepository.findByUsername(request.getUsername()).orElse(null);

        if (user == null) {
            return ResponseEntity.status(401).body("Usuario no encontrado");
        }

          // Bloquear acceso a administradores
         if ("ADMIN".equalsIgnoreCase(user.getRole())) {
       return ResponseEntity.status(HttpStatus.FORBIDDEN).body(
        Map.of("message", "Los administradores no pueden acceder a esta aplicación")
        );}

        if (!passwordEncoder.matches(request.getPassword(), user.getPassword())) {
            return ResponseEntity.status(401).body("Contraseña incorrecta");
        }

        String token = jwtService.generateToken(user.getUsername());

        String role = user.getRole(); 

String redirectUrl;

switch (role.toUpperCase()) {
    
    case "SUPERVISOR":
        redirectUrl = "/panel";
        break;
    case "OPERARIO":
        redirectUrl = "/panel";
        break;
    default:
        redirectUrl = "/";
}

LoginResponse response = new LoginResponse(
        token,
        user.getUsername(),
        role,
        redirectUrl
);

return ResponseEntity.ok(response);
    }
}