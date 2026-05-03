package com.example.demo.service;

import com.example.demo.dto.NewUserRequest;
import com.example.demo.entity.User;
import com.example.demo.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

/**
 * Servicio encargado de crear nuevos usuarios aplicando las validaciones y el cifrado de contrasena correspondiente.
 */
@Service
public class NewUserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    /**
     * Crea un nuevo usuario aplicando cifrado de contrasena.
     */
    public User createUser(NewUserRequest request) {

        if (userRepository.existsByUsername(request.getUsername())) {
            throw new RuntimeException("Usuario ocupado");
        }

        if (userRepository.existsByEmail(request.getEmail())) {
            throw new RuntimeException("Email ya ocupado");
        }

        User user = new User();

        user.setUsername(request.getUsername());
        user.setEmail(request.getEmail());
        user.setPassword(passwordEncoder.encode(request.getPassword()));

        return userRepository.save(user);
    }
}
