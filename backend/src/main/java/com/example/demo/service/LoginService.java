package com.example.demo.service;

import com.example.demo.entity.User;
import com.example.demo.repository.UserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

/**
 * Servicio responsable de validar las credenciales introducidas en el inicio de sesion web.
 */
@Service
public class LoginService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public LoginService(UserRepository userRepository,
                        PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    /**
     * Valida las credenciales de un usuario y devuelve el usuario autenticado.
     */
    public User login(String username, String password) {

        Optional<User> userOptional = userRepository.findByUsername(username);

        if (userOptional.isEmpty()) {
            return null;
        }

        User user = userOptional.get();

        if (passwordEncoder.matches(password, user.getPassword())) {
            return user;
        }

        return null;
    }
}
