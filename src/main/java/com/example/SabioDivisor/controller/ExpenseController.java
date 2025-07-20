package com.example.SabioDivisor.controller;

import com.example.SabioDivisor.model.AppUser;
import com.example.SabioDivisor.model.Debt;
import com.example.SabioDivisor.model.Expense;
import com.example.SabioDivisor.service.AppUserService;
import com.example.SabioDivisor.service.ExpenseService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
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
        model.addAttribute("expenses",expenseService.findAllByUser(loggedUser));
        return "expenses/list";
    }

    @GetMapping("/new")
    public String newExpense(Model model, HttpSession session) {
        AppUser loggedUser = (AppUser) session.getAttribute("loggedUser");
        if (loggedUser == null) {
            return "redirect:/login";
        }
        //Agrega los Maps para los pagadores y deudores, porque el html los espera para mostrar los campos de pago y deuda en el caso que sea edicion.
        model.addAttribute("payers", new HashMap<Long, Double>());
        model.addAttribute("debtors", new HashMap<Long, Double>());

        model.addAttribute("expense", new Expense());//Crea un nuevo objeto Expense para que el formulario pueda llenarlo.
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

        //VALIDACIONES: Si el gasto le falta algo o es inválido, se muestra un mensaje de error y se vuelve al formulario.
        if (expense.getDate() == null || expense.getDate().isAfter(LocalDate.now())) {
            model.addAttribute("error", "La fecha del gasto no puede ser nula o futura.");
            model.addAttribute("expense", expense);
            model.addAttribute("users", appUserService.listAll());
            return "expenses/form";
        }

        if (expense.getAmount() <= 0) {
            model.addAttribute("error", "El monto del gasto debe ser mayor a cero.");
            model.addAttribute("expense", expense);
            model.addAttribute("users", appUserService.listAll());
            return "expenses/form";
        }

        if (expense.getInstallments() < 1 || expense.getInstallments() > 24) {//Se limita a 24 cuotas para evitar deudas eternas.
            model.addAttribute("error", "Las cuotas no pueden ser menores a 1 o mayores a 24.");
            model.addAttribute("expense", expense);
            model.addAttribute("users", appUserService.listAll());
            return "expenses/form";
        }

        Map<Long,Double> payers = new HashMap<>();
        Map<Long,Double> debtors = new HashMap<>();
        try{
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
            model.addAttribute("payers", payers);
            model.addAttribute("debtors", debtors);
            return "expenses/form";
        }
    }

    @GetMapping("/edit/{id}")
    public String editExpense(Model model, HttpSession session, @PathVariable Long id) {
        AppUser loggedUser = (AppUser) session.getAttribute("loggedUser");
        if (loggedUser == null) {
            return "redirect:/login";
        }

        Expense expense = expenseService.findById(id);

        //VALIDACIONES: Si el gasto no existe o si el usuario no participó en el gasto, se muestra un mensaje de error y se redirige a la lista de gastos.
        if (expense == null) {
            model.addAttribute("error", "Gasto no encontrado.");
            model.addAttribute("expenses", expenseService.findAllByUser(loggedUser));
            return "expenses/list";
        }
        if(!expenseService.userParticipatedInExpense(loggedUser, expense.getId())) {
            model.addAttribute("error", "No puedes editar un gasto en el que no participaste.");
            model.addAttribute("expenses", expenseService.findAllByUser(loggedUser));
            return "expenses/list";
        }
        //CONTINUA EL FLUJO

        //AGREGA LAS DEUDAS Y PAGOS AL GASTO PARA QUE SE MUESTREN EN EL FORMULARIO DE EDICIÓN.
        Map<Long, Double> payers = new HashMap<>();
        Map<Long, Double> debtors = new HashMap<>();
        List<Debt> debts = expenseService.findDebtsByExpenseId(expense.getId());

        for (Debt debt : debts) {
            Long creditorId = debt.getCreditor().getId();
            Long debtorId = debt.getDebtor().getId();

            payers.put(creditorId, (payers.getOrDefault(creditorId,0.0) + debt.getAmount()));// Si hay mas de una cuota el getOrDefault va a traer el monto de la iteración previa y acumula el de la nueva cuota, si es la primera o la ultima trae el default (0.0).
            debtors.put(debtorId, (debtors.getOrDefault(debtorId,0.0) + debt.getAmount()));// Lo mismo que arriba, pero para los deudores.
        }

        model.addAttribute("expense", expense);
        model.addAttribute("users", appUserService.listAll());
        model.addAttribute("payers", payers);
        model.addAttribute("debtors", debtors);
        return "expenses/form";
    }

    @GetMapping("/delete/{id}")
    public String delete(Model model,@PathVariable Long id, HttpSession session, RedirectAttributes redirectAttributes) {
        AppUser loggedUser = (AppUser) session.getAttribute("loggedUser");
        if (loggedUser == null) {
            return "redirect:/login";
        }

        Expense expense = expenseService.findById(id);

        //VALIDACIONES: Si el gasto no existe o si el usuario no participó en el gasto, se muestra un mensaje de error y se redirige a la lista de gastos.
        if (expense == null) {
            redirectAttributes.addFlashAttribute("error", "Gasto no encontrado.");
            return "redirect:/expenses";
        }
        if(!expenseService.userParticipatedInExpense(loggedUser, expense.getId())) {
            redirectAttributes.addFlashAttribute("error", "No puedes eliminar un gasto en el que no participaste.");
            return "redirect:/expenses";
        }
        //CONTINUA EL FLUJO

        expenseService.delete(expense.getId());
        redirectAttributes.addFlashAttribute("success", "Gasto eliminado correctamente.");//redirect.Atributtes.addFlashAttribute: Guarda temporalmente un atributo (ej. "error") y lo pasa al siguiente redirect. Es "flash" porque se elimina después de que se muestra una vez.
        return "redirect:/expenses";
    }

}
