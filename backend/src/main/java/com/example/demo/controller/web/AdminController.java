package com.example.demo.controller.web;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.example.demo.entity.User;
import com.example.demo.repository.UserRepository;

/**
 * Controlador web encargado de mostrar y gestionar la zona de administracion.
 */
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
public String saveUser(@ModelAttribute("user") User user,
                       BindingResult result,
                       Model model) {

    if (userRepository.existsByUsername(user.getUsername())) {
        result.rejectValue("username", "error.username", "Ese nombre de usuario ya existe");
    }

    if (userRepository.existsByEmail(user.getEmail())) {
        result.rejectValue("email", "error.email", "Ese correo electrónico ya existe");
    }

    if (result.hasErrors()) {
        model.addAttribute("modoEdicion", false);
        return "user-form";
    }

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
public String updateUser(@ModelAttribute("user") User user,
                         BindingResult result,
                         Model model) {

    if (userRepository.existsByUsernameAndIdNot(user.getUsername(), user.getId())) {
        result.rejectValue("username", "error.username", "Ese nombre de usuario ya existe");
    }

    if (userRepository.existsByEmailAndIdNot(user.getEmail(), user.getId())) {
        result.rejectValue("email", "error.email", "Ese correo electrónico ya existe");
    }

    if (result.hasErrors()) {
        model.addAttribute("modoEdicion", true);
        return "user-form";
    }

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
