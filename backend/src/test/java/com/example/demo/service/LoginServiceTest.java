package com.example.demo.service;

import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.mockito.Mockito.when;

import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.example.demo.entity.User;
import com.example.demo.repository.UserRepository;

/**
 * Pruebas unitarias de LoginService.
 * Verifican el comportamiento esperado sin modificar la logica de produccion.
 */
@ExtendWith(MockitoExtension.class)
class LoginServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private LoginService loginService;

    /**
     * Comprueba el caso de prueba: loginReturnsUserWhenPasswordMatches.
     */
    @Test
    void loginReturnsUserWhenPasswordMatches() {
        User user = new User();
        user.setUsername("admin");
        user.setPassword("encoded-password");

        when(userRepository.findByUsername("admin")).thenReturn(Optional.of(user));
        when(passwordEncoder.matches("plain-password", "encoded-password")).thenReturn(true);

        User result = loginService.login("admin", "plain-password");

        assertSame(user, result);
    }

    /**
     * Comprueba el caso de prueba: loginReturnsNullWhenUserDoesNotExist.
     */
    @Test
    void loginReturnsNullWhenUserDoesNotExist() {
        when(userRepository.findByUsername("missing")).thenReturn(Optional.empty());

        User result = loginService.login("missing", "plain-password");

        assertNull(result);
    }

    /**
     * Comprueba el caso de prueba: loginReturnsNullWhenPasswordDoesNotMatch.
     */
    @Test
    void loginReturnsNullWhenPasswordDoesNotMatch() {
        User user = new User();
        user.setPassword("encoded-password");

        when(userRepository.findByUsername("admin")).thenReturn(Optional.of(user));
        when(passwordEncoder.matches("wrong-password", "encoded-password")).thenReturn(false);

        User result = loginService.login("admin", "wrong-password");

        assertNull(result);
    }
}
