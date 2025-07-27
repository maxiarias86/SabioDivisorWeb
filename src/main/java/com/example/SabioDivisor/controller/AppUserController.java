package com.example.SabioDivisor.controller;

import com.example.SabioDivisor.model.AppUser;
import com.example.SabioDivisor.service.AppUserService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/users")
public class AppUserController {
    @Autowired
    private AppUserService service;

    @GetMapping
    public String list(Model model) {
        model.addAttribute("users", service.listAll());
        return "users/list"; // Esto apunta a la vista Thymeleaf en src/main/resources/templates/users/list.html
    }

    @GetMapping("/new")
    public String add(Model model) {
        model.addAttribute("user", new AppUser()); // Metodo para crear un nuevo usuario
        return "users/form"; // Esto apunta a la vista Thymeleaf en src/main/resources/templates/users/form.html
    }

    @GetMapping("/edit")
    public String edit(Model model, HttpSession session) {
        AppUser loggedUser = (AppUser) session.getAttribute("loggedUser");
        if (loggedUser == null) {
            return "redirect:/login"; // Redirige al login si no hay usuario logueado
        }
        model.addAttribute("user", service.findById(loggedUser.getId()));// No se pasa directamente el loggedUser, sino que se busca por ID porque es más robusto y seguro trabajar con un objeto directo de la BBDD.
        return "users/form";
    }

    @PostMapping("/save")
    public String save(AppUser user) {
        service.save(user);
        return "redirect:/login"; // Redirige a la lista de usuarios después de guardar
    }
    //No se puede eliminar usuarios.
}
