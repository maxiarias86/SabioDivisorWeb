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

@Controller
@RequestMapping("/debts")
public class DebtController {

    @Autowired
    private DebtService debtService;

    @Autowired
    private ExpenseService expenseService;

    @GetMapping("/list/{expenseId}")
    public String list(Model model, HttpSession session, @PathVariable Long expenseId) {
        AppUser loggedUser = (AppUser) session.getAttribute("loggedUser");
        if (loggedUser == null) {
            return "redirect:/login"; // Redirige al login si el usuario no está logueado
        }

        if (expenseId == null) {
            model.addAttribute("error", "Falta el ID del gasto"); // Agrega un mensaje de error si el ID del gasto es nulo
            return "debts/list"; // Retorna a la vista de lista de deudas con el mensaje de error
        }

        if (!expenseService.userParticipatedInExpense((AppUser) session.getAttribute("loggedUser"), expenseId)) {
            model.addAttribute("error", "El usuario no participó en el gasto con ID " + expenseId);
            return "debts/list"; // Retorna a la vista de lista de deudas con el mensaje de error
        }

        model.addAttribute("debts", debtService.findDebtsByExpenseIdAndUserId(loggedUser.getId(),expenseId));
        return "debts/list"; // Esto apunta a la vista Thymeleaf en src/main/resources/templates/debts/list.html
    }







}
