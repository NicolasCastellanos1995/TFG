package com.example.demo.controller.web;

import com.example.demo.entity.User;
import com.example.demo.repository.UserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/admin")
public class AdminController {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public AdminController(UserRepository userRepository,
                           PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @GetMapping
    public String adminPanel(@RequestParam(required = false) String keyword, Model model) {

        if (keyword != null && !keyword.isBlank()) {
            model.addAttribute("users",
                    userRepository.findByUsernameContainingIgnoreCaseOrEmailContainingIgnoreCase(keyword, keyword));
        } else {
            model.addAttribute("users", userRepository.findAll());
        }

        model.addAttribute("keyword", keyword);
        return "admin";
    }

    @GetMapping("/users/new")
    public String newUserForm(Model model) {
        model.addAttribute("user", new User());
        model.addAttribute("modoEdicion", false);
        return "user-form";
    }

    @PostMapping("/users/save")
    public String saveUser(@ModelAttribute User user) {
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        if (user.getActive() == null) {
            user.setActive(true);
        }
        userRepository.save(user);
        return "redirect:/admin";
    }

    @GetMapping("/edit/{id}")
    public String editUserForm(@PathVariable Long id, Model model) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Usuario no encontrado con id: " + id));

        model.addAttribute("user", user);
        model.addAttribute("modoEdicion", true);
        return "user-form";
    }

    @PostMapping("/users/update")
    public String updateUser(@ModelAttribute User user) {
        User userExistente = userRepository.findById(user.getId())
                .orElseThrow(() -> new IllegalArgumentException("Usuario no encontrado con id: " + user.getId()));

        userExistente.setUsername(user.getUsername());
        userExistente.setEmail(user.getEmail());
        userExistente.setRole(user.getRole());
        userExistente.setActive(user.getActive());

        if (user.getPassword() != null && !user.getPassword().isBlank()) {
            userExistente.setPassword(passwordEncoder.encode(user.getPassword()));
        }

        userRepository.save(userExistente);
        return "redirect:/admin";
    }

    @GetMapping("/users/delete/{id}")
    public String deleteUser(@PathVariable Long id) {
        userRepository.deleteById(id);
        return "redirect:/admin";
    }
}