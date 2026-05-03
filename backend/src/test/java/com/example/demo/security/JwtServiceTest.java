package com.example.demo.security;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

class JwtServiceTest {

    private final JwtService jwtService = new JwtService();

    @Test
    void generateTokenCreatesValidTokenWithUsername() {
        String token = jwtService.generateToken("operador");

        assertNotNull(token);
        assertTrue(jwtService.isTokenValid(token));
        assertEquals("operador", jwtService.extractUsername(token));
    }

    @Test
    void isTokenValidReturnsFalseForInvalidToken() {
        assertFalse(jwtService.isTokenValid("token-invalido"));
    }
}
