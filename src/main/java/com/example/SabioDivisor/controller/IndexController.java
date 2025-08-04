package com.example.SabioDivisor.controller;

import com.example.SabioDivisor.model.AppUser;
import com.example.SabioDivisor.model.Debt;
import com.example.SabioDivisor.model.Payment;
import com.example.SabioDivisor.service.AppUserService;
import com.example.SabioDivisor.service.BalanceService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.time.LocalDate;
import java.util.List;

@Controller
public class IndexController {

    @Autowired
    private BalanceService balanceService;

    // Este controlador maneja la ruta raíz y redirige a la página de inicio
    @RequestMapping("/")
    public String redirectToIndex() {
        return "redirect:/index";
    }

    @GetMapping("/index")
    public String index(HttpSession session, Model model, @RequestParam(required = false) LocalDate date) {//required=false permite que la fecha sea opcional, si no se pasa, se usa la fecha actual. Se puede pasar en la URL como ?date=YYYY-MM-DD
        AppUser user = (AppUser) session.getAttribute("loggedUser");
        if (user == null) {
            return "redirect:/login";
        }
        if(date == null) {
            date = LocalDate.now(); // Si no se pasa una fecha, se usa la fecha actual
        }

        model.addAttribute("date", date);
        model.addAttribute("user", user);
        try{
            model.addAttribute("balance", balanceService.getBalance(balanceService.getUserBalances(user, date)));
            model.addAttribute("balances", balanceService.getUserBalances(user, date)); // Obtiene los balances del usuario actual
        } catch (IllegalArgumentException e) {
            model.addAttribute("error", e.getMessage());
        }
        return "index"; // Esto renderiza templates/index.html
        }

}
