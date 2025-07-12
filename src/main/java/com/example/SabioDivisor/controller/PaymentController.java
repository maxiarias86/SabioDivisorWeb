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
    public String list(Model model, @RequestParam(name = "userId") Long userId) {//Se usa @RequestParam para recibir el ID del usuario desde la URL o un formulario
        model.addAttribute("payments", service.findAllByUserId(userId));
        return "payments/list"; // Esto apunta a la vista Thymeleaf en src/main/resources/templates/payments/list.html
    }

    @GetMapping("/new")
    public String add(Model model, @RequestParam(name = "userId") Long userId) {
        model.addAttribute("payment", new Payment());
        model.addAttribute("users", userService.listAll()); // Para mostrar los usuarios en el formulario
        model.addAttribute("userId", userId); // Para mantener el ID del usuario en el formulario
        return "payments/form"; // Esto apunta a la vista Thymeleaf en src/main/resources/templates/payments/form.html
    }

    @GetMapping("/edit/{paymentId}")//El id es parte de la ruta de la URL, por lo que se usa @PathVariable
    public String edit(Model model, @PathVariable Long paymentId, @RequestParam(name = "userId") Long userId) {//Se usa @PathVariable para recibir el ID del pago como parte de la URL
        model.addAttribute("payment", service.findById(paymentId));
        model.addAttribute("users", userService.listAll()); // Para mostrar los usuarios en el formulario
        model.addAttribute("userId", userId); // Para mantener el ID del usuario en el formulario
        return "payments/form";
    }

    @PostMapping("/save")
    public String save(Model model, Payment payment, @RequestParam(name = "userId") Long userId) {
        //Validaciones del formulario en la capa Controller
        if (payment.getAmount() <= 0) {
            model.addAttribute("error", "El monto del pago debe ser mayor a cero.");
            model.addAttribute("users", userService.listAll());
            model.addAttribute("userId", userId); // Para mantener el ID del usuario en el formulario
            return "payments/form"; // Retorna al formulario si hay un error de validación
        }
        if (payment.getPayer().getId().equals(payment.getRecipient().getId())) {
            model.addAttribute("error", "El pagador no puede ser el mismo que el destinatario.");
            model.addAttribute("users", userService.listAll());
            model.addAttribute("userId", userId); // Para mantener el ID del usuario en el formulario

            return "payments/form"; // Retorna al formulario si hay un error de validación
        }
        if (!payment.getPayer().getId().equals(userId) && !payment.getRecipient().getId().equals(userId)) {
            model.addAttribute("error", "No puedes agregar pagos en los que no participes.");
            model.addAttribute("users", userService.listAll());
            model.addAttribute("userId", userId); // Para mantener el ID del usuario en el formulario

            model.addAttribute("payment", payment);
            return "payments/form"; // Retorna al formulario si hay un error de validación
        }

        service.save(payment);
        return "redirect:/payments?userId=" + userId;// Redirige a la lista de pagos después de guardar con el id de usuario como @RequestParam
    }

    @GetMapping("/delete/{paymentId}")
    public String delete(@PathVariable Long paymentId, @RequestParam(name = "userId") Long userId) {
        service.delete(paymentId);
        return "redirect:/payments?userId=" + userId;// Redirige a la lista de pagos después de guardar con el id de usuario como @RequestParam
    }

}

