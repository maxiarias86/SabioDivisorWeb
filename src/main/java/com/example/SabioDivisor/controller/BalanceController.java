package com.example.SabioDivisor.controller;

import com.example.SabioDivisor.dto.BalanceDTO;
import com.example.SabioDivisor.model.AppUser;
import com.example.SabioDivisor.service.AppUserService;
import com.example.SabioDivisor.service.BalanceService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

import java.time.LocalDate;

@Controller
public class BalanceController {

    @Autowired
    private BalanceService balanceService;

    @Autowired
    private AppUserService appUserService;


    @GetMapping("/balance/{friendId}")
    public String getBalanceWithFriend(HttpSession session, Model model, @PathVariable Long friendId, @RequestParam(required = false) LocalDate date) {
        AppUser user = (AppUser) session.getAttribute("loggedUser");
        if (user == null) {
            return "redirect:/login";
        }
        if(date == null) {
            date = LocalDate.now(); // Si no se pasa una fecha, se usa la fecha actual
        }

        AppUser friend = appUserService.findById(friendId);
        if (friend == null) {
            model.addAttribute("error", "Amigo no encontrado");
            return "redirect:/index"; // Redirige a la página de inicio si el amigo no existe
        }

        try{
            BalanceDTO balanceDTO = balanceService.getBalanceDTO(user, friend, date);

            model.addAttribute("date", date);
            model.addAttribute("user", user);
            model.addAttribute("balanceDTO", balanceDTO);

            return "balances/balance"; // Esto renderiza templates/balance.html
        } catch (IllegalArgumentException e) {
            model.addAttribute("error", e.getMessage());
            return "redirect:/index"; // Redirige a la página de inicio si hay un error
        }

    }
}
