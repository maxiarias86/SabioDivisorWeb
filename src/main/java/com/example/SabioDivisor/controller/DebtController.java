package com.example.SabioDivisor.controller;

import com.example.SabioDivisor.model.AppUser;
import com.example.SabioDivisor.service.DebtService;
import com.example.SabioDivisor.service.ExpenseService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller//Es un controlador de Spring MVC que maneja solicitudes web y devuelve vistas. Escucha todas las solicitudes que empiezan con /debts
@RequestMapping("/debts")
public class DebtController {

    @Autowired//Inyección de dependencia del DebtService. Crea automáticamente una instancia de esta clase en el Controller.
    private DebtService debtService;

    @Autowired
    private ExpenseService expenseService;

    @GetMapping("/list/{expenseId}")
    public String list(Model model, HttpSession session, @PathVariable Long expenseId) {
        AppUser loggedUser = (AppUser) session.getAttribute("loggedUser");
        if (loggedUser == null) {
            return "redirect:/login"; // Redirige al login si el usuario no está logueado
        }

        try{
            if (!expenseService.userParticipatedInExpense((AppUser) session.getAttribute("loggedUser"), expenseId)) {//Si el expenseId es null lanza una IllegalArgumentException en el Service
                model.addAttribute("error", "El usuario no participó en el gasto con ID " + expenseId);
                return "debts/list"; // Retorna a la vista de lista de deudas con el mensaje de error
            }

            model.addAttribute("debts", debtService.findDebtsByExpenseIdAndUserId(loggedUser.getId(),expenseId));
            return "debts/list"; // Esto apunta a la vista Thymeleaf en src/main/resources/templates/debts/list.html

        } catch (IllegalArgumentException e) {
            model.addAttribute("error", e.getMessage());
            return "debts/list"; // Retorna a la vista de lista de deudas con el mensaje de error
        }
    }







}
