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

    @GetMapping("/edit/{id}")
    public String edit(Model model, @PathVariable Long id) {
        model.addAttribute("user", service.findById(id));
        return "users/form";
    }

    @PostMapping("/save")
    public String save(AppUser user) {
        service.save(user);
        return "redirect:/users"; // Redirige a la lista de usuarios después de guardar
    }

    @GetMapping("/delete/{id}")
    public String delete(@PathVariable Long id) {
        service.delete(id);
        return "redirect:/users"; // Redirige a la lista de usuarios después de eliminar
    }

    @GetMapping("/index")
    public String index(HttpSession session, Model model) {
        System.out.println("Entrando al index");
        AppUser user = (AppUser) session.getAttribute("loggedUser");// Obtiene el usuario logueado de la sesión
        if (user == null) {// Si no hay un usuario logueado, redirige al formulario de inicio de sesión
            return "redirect:/login";
        }
        model.addAttribute("user", user);// Agrega el usuario logueado al modelo para que esté disponible en la vista
        return "index";// Retorna al index.
    }


}
