package com.example.SabioDivisor.controller;

import com.example.SabioDivisor.model.AppUser;
import com.example.SabioDivisor.model.Expense;
import com.example.SabioDivisor.service.AppUserService;
import com.example.SabioDivisor.service.ExpenseService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

@Controller
@RequestMapping("/expenses")
public class ExpenseController {

    @Autowired
    private ExpenseService expenseService;

    @Autowired
    private AppUserService appUserService;

    @GetMapping
    public String list(Model model, HttpSession session) {
        AppUser loggedUser = (AppUser) session.getAttribute("loggedUser");
        if (loggedUser == null) {//Si el usuario no esta logueado vuelve al login
            return "redirect:/login";
        }

        //CREAR EL METODO QUE DEVUELVE TODOS LOS EXPENSES DEL USUARIO LOGUEADO
        model.addAttribute("expenses",new ArrayList<>());
        return "expenses/list";
    }

    @GetMapping("/new")
    public String newExpense(Model model, HttpSession session) {
        AppUser loggedUser = (AppUser) session.getAttribute("loggedUser");
        if (loggedUser == null) {
            return "redirect:/login";
        }
        model.addAttribute("expense", new Expense());
        model.addAttribute("users",appUserService.listAll());
        return "expenses/form";
    }

    @PostMapping("/save")
    public String saveExpense(
            @ModelAttribute Expense expense, //Anotación para que Spring llene el objeto con los valores del formulario
            Model model,
            HttpSession session,
            @RequestParam Map<String, String> params//Captura los parámetros del formulario como un Map.
    ) {
        AppUser loggedUser = (AppUser) session.getAttribute("loggedUser");
        if (loggedUser == null) {
            return "redirect:/login";
        }
        try{
            Map<Long,Double> payers = new HashMap<>();
            Map<Long,Double> debtors = new HashMap<>();

            for (String key : params.keySet()) {//Recorre todos los parámetros del formulario para buscar claves que empiecen con: "payer_": representa un pagador. "debtor_": representa un deudor.
                if (key.startsWith("payer_")) {
                    Long id = Long.parseLong(key.substring(6));//El id es lo que le sigue al sexto caracter (luego de "payer_")
                    Double amount = Double.parseDouble(params.get(key));//Obtiene el monto que pago
                    if (amount > 0) {
                        payers.put(id, amount);//Lo guarda en el Map payers
                    }
                }
                if (key.startsWith("debtor_")) {
                    Long id = Long.parseLong(key.substring(7));
                    Double amount = Double.parseDouble(params.get(key));
                    if (amount > 0) {
                        debtors.put(id, amount);
                    }
                }
            }
            expenseService.save(expense,payers,debtors);
            return "redirect:/expenses";
        } catch (Exception e) {//Si se lanza una excepción (por ejemplo, el monto no coincide o hay datos faltantes), se muestra el formulario otra vez con: el error, el gasto cargado previamente, La lista de usuarios para volver a seleccionar pagadores/deudores.
            model.addAttribute("error", e.getMessage());
            model.addAttribute("expense", expense);
            model.addAttribute("users", appUserService.listAll());
            return "expenses/form";
        }
    }

}
