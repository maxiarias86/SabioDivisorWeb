package com.example.SabioDivisor.controller;

import com.example.SabioDivisor.model.AppUser;
import com.example.SabioDivisor.service.AppUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/users")
public class UserController {
    @Autowired
    private AppUserService service;

    @GetMapping
    public String list(Model model) {
        model.addAttribute("users", service.listAll());
        return "users/list"; // Esto apunta a la vista Thymeleaf en src/main/resources/templates/users/list.html
    }

    @GetMapping
    public String add(Model model) {
        model.addAttribute("user", new AppUser()); // Metodo para crear un nuevo usuario
        return "users/form"; // Esto apunta a la vista Thymeleaf en src/main/resources/templates/users/form.html
    }
}
