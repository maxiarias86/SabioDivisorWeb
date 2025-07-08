package com.example.SabioDivisor.controller;

import com.example.SabioDivisor.model.Payment;
import com.example.SabioDivisor.service.AppUserService;
import com.example.SabioDivisor.service.PaymentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/payments")
public class PaymentController {
    @Autowired
    private PaymentService service;

    @Autowired
    private AppUserService userService;

    @GetMapping
    public String list(Model model, @RequestParam(name = "userId") Long id) {
        model.addAttribute("payments", service.findAllByUserId(id));
        return "payments/list"; // Esto apunta a la vista Thymeleaf en src/main/resources/templates/payments/list.html
    }

    @GetMapping("/new")
    public String add(Model model) {
        model.addAttribute("payment", new Payment());
        model.addAttribute("users", userService.listAll()); // Para mostrar los usuarios en el formulario
        return "payments/form"; // Esto apunta a la vista Thymeleaf en src/main/resources/templates/payments/form.html
    }

    @GetMapping("/edit/{id}")
    public String edit(Model model, @PathVariable Long id) {
        model.addAttribute("payment", service.findById(id));
        model.addAttribute("users", userService.listAll()); // Para mostrar los usuarios en el formulario
        return "payments/form";
    }

    @PostMapping("/save")
    public String save(Model model, Payment payment, @RequestParam Long userId) {
        //Validaciones del formulario en la capa Controller
        if (payment.getAmount() <= 0) {
            model.addAttribute("error", "El monto del pago debe ser mayor a cero.");
            model.addAttribute("users", userService.listAll());
            return "payments/form"; // Retorna al formulario si hay un error de validación
        }
        if (payment.getPayer().getId().equals(payment.getRecipient().getId())) {
            model.addAttribute("error", "El pagador no puede ser el mismo que el destinatario.");
            model.addAttribute("users", userService.listAll());
            return "payments/form"; // Retorna al formulario si hay un error de validación
        }
        if (!payment.getPayer().getId().equals(userId) && !payment.getRecipient().getId().equals(userId)) {
            model.addAttribute("error", "No puedes agregar pagos en los que no participes.");
            model.addAttribute("users", userService.listAll());
            model.addAttribute("payment", payment);
            return "payments/form"; // Retorna al formulario si hay un error de validación
        }

        service.save(payment);
        return "redirect:/payments"; // Redirige a la lista de pagos después de guardar
    }

    @GetMapping("/delete/{id}")
    public String delete(@PathVariable Long id) {
        service.delete(id);
        return "redirect:/payments"; // Redirige a la lista de pagos después de eliminar
    }
}
