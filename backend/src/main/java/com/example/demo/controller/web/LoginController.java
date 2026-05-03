package com.example.demo.controller.web;

import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

/**
 * Controlador web encargado de mostrar la pantalla de inicio de sesion.
 */
@Controller
public class LoginController {

    @GetMapping("/")
    /**
     * Valida las credenciales de un usuario y devuelve el usuario autenticado.
     */
    public String login() {
        return "login";
    }

    @GetMapping("/redirect-by-role")
    public String redirectByRole(Authentication authentication) {

        boolean admin = authentication.getAuthorities().stream()
                .anyMatch(a -> a.getAuthority().equals("ROLE_ADMIN"));

        if (admin) {
            return "redirect:/admin";
        }

        return "redirect:/dashboard";
    }
}
