package com.example.SabioDivisor.controller;

import com.example.SabioDivisor.model.AppUser;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class IndexController {
    // Este controlador maneja la ruta raíz y redirige a la página de inicio
    @RequestMapping("/")
    public String redirectToIndex() {
        return "redirect:/index"; // Redirige a la lista de deudas
    }

    @GetMapping("/index")
    public String index(HttpSession session, Model model) {
        //ELIMINAR EL SYSO ANTES DE ENTREGAR
        System.out.println("Entrando al index");
        AppUser user = (AppUser) session.getAttribute("loggedUser");
        if (user == null) {
            return "redirect:/login";
        }
        model.addAttribute("user", user);
        return "index"; // Esto renderiza templates/index.html
        }
}
