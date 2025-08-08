package com.example.SabioDivisor.controller;

import com.example.SabioDivisor.model.AppUser;
import com.example.SabioDivisor.service.AppUserService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class LoginController {

    @Autowired
    private AppUserService userService;

    @GetMapping("/login")
    public String loginForm() {
        return "login";
    }

    @PostMapping("/login")
    public String login(@RequestParam String email, @RequestParam String password, Model model, HttpSession session) {
        /*
        HttpSession es una interfaz que permite almacenar información del usuario sin tener que pasarla por la URL.
         */
        try{
            AppUser user = userService.validateCredentials(email, password);//Me devuelve un usuario si las credenciales son correctas.
            if (user == null) {
                model.addAttribute("error", "Email o contraseña incorrectos");
                return "login";//Si las credenciales son incorrectas, se muestra el mensaje de error y se vuelve al formulario de inicio de sesión.
            }
            session.setAttribute("loggedUser", user);// Almacena el usuario logueado en la sesión actual. Esto permite que otros Controllers puedan acceder a este usuario sin tener que volver a autenticarlo y sin tener que pasarlo por la URL.
            return "redirect:/index"; // Redirige a la página de inicio después de un inicio de sesión exitoso.
        } catch (IllegalArgumentException e) {
            model.addAttribute("error", "Error al iniciar sesión: " + e.getMessage());
            return "login"; // Si ocurre un error, se muestra el mensaje de error y se vuelve al formulario de inicio de sesión.
        }
    }

    @GetMapping("/logout")
    public String logout(HttpSession session) {
        session.invalidate(); // Invalida la sesión actual, eliminando al usuario logueado.
        return "redirect:/login"; // Redirige al formulario de inicio de sesión después de cerrar sesión.
    }
}
